# 위험 기반 검증 지침

변경과 관련된 시나리오만 선택하고, 검증하려는 계약과 기대 결과를 먼저 적어주세요. 순수 계산은 JVM 단위 테스트로, 실제 프레임·레이아웃·제스처에 의존하는 동작은 Compose 통합 테스트로 검증해주세요. 테스트를 새로 구현하는 것은 해당 작업 범위에 포함될 때만 수행해주세요.

## 1. 순수 경로·앵커·선택 규칙

대상은 [SectionPath](../../../../section-sync/src/main/java/io/github/tenboard/section_sync/SectionPath.kt), [SectionAnchor](../../../../section-sync/src/main/java/io/github/tenboard/section_sync/SectionAnchor.kt), [SectionResolver](../../../../section-sync/src/main/java/io/github/tenboard/section_sync/SectionResolver.kt)입니다.

| 선택할 상황 | JVM에서 확인할 결과 |
| --- | --- |
| 빈 앵커 목록 | 선택 결과가 `null`인지 확인해주세요. |
| 음수·중복·역순 인덱스, 중복 경로 | 음수는 앵커 생성 시, 나머지는 앵커 검증 경로에서 `IllegalArgumentException`인지 확인해주세요. |
| 중첩 경로, 없는 레벨 | 0부터 시작하는 각 레벨의 키, 음수·범위 밖 레벨과 활성 경로 부재의 `null` 결과를 확인해주세요. |
| 첫 앵커 이전·섹션 경계 | `FollowViewport`에서 첫 앵커 전은 `null`, 경계 인덱스는 그 앵커, 사이 위치는 직전 앵커인지 확인해주세요. |
| 짧은 콘텐츠와 끝 정책이 함께 적용될 조건 | 양방향 스크롤 불가일 때 short 정책이 end 정책보다 우선하는지 확인해주세요. |
| 경계 선택 옵션 변경 | short/end 각각의 `FirstAnchor`, `LastAnchor`, `FollowViewport` 결과를 구별해주세요. |

경계 정책의 비교용 데이터는 A=2, B=5, C=9이고 첫 보이는 인덱스는 6으로 두실 수 있습니다. 기대 결과는 다음과 같습니다. 정책이 발동하지 않는 일반 위치에서는 항상 B입니다.

| canScrollForward | canScrollBackward | 선택하는 정책 | FirstAnchor / LastAnchor / FollowViewport |
| --- | --- | --- | --- |
| false | false | shortContentSelection | A / C / B |
| false | true | endOfContentSelection | A / C / B |
| true | true | 일반 뷰포트 규칙 | 옵션과 무관하게 B |

정책 우선순위는 short=`FirstAnchor`, end=`LastAnchor`처럼 서로 다른 값으로 확인해주세요. 입력·출력 예시가 필요하면 Kotlin을 사용해주세요. 아래는 향후 테스트 입력 예시이며 현재 존재하거나 실행된 테스트가 아닙니다.

```kotlin
val anchors = listOf(
    SectionAnchor(SectionPath.of("A"), firstItemIndex = 2),
    SectionAnchor(SectionPath.of("B"), firstItemIndex = 5),
    SectionAnchor(SectionPath.of("C"), firstItemIndex = 9),
)
```

## 2. Compose·코루틴 연동

대상은 [SectionSyncState](../../../../section-sync/src/main/java/io/github/tenboard/section_sync/SectionSyncState.kt), [RememberSectionSyncState](../../../../section-sync/src/main/java/io/github/tenboard/section_sync/RememberSectionSyncState.kt), [샘플 화면](../../../../app/src/main/java/io/github/tenboard/composesectionsync/ui/MenuSampleScreen.kt)입니다. 아래 항목은 현재 구현의 성공을 선언하는 목록이 아니라 변경별로 확인할 계약입니다.

| 위험 | 확인할 행동과 결과 |
| --- | --- |
| 양방향 동기화 | 탭 요청이 해당 아이템으로 이동하고, 사용자 스크롤이 중첩 경로의 활성 탭을 갱신하는지 확인해주세요. 즉시 이동과 애니메이션 이동을 변경 범위에 맞게 선택해주세요. |
| 빠른 연속 요청·진행 중 스크롤 | `InterruptAndProceed`에서는 새 요청이 진행되고 이전 완료가 새 선택을 덮지 않는지 확인해주세요. `InterruptAndDiscardRequest`에서는 진행 중 이동을 멈추고 새 요청을 폐기하는지, 사용자 제스처/기존 프로그램 이동 각각을 확인해주세요. |
| 취소 전파 | 이전 요청을 취소한 후에도 호출자의 상위 Job과 관련 없는 형제 작업이 살아 있는지 확인해주세요. 요청별 작업과 호출자 수명을 구분해주세요. |
| 앵커 변경·gridState 교체·Composition 이탈 | 기존 요청과 관찰의 정리, 낡은 상태의 후속 갱신 방지, 새 그리드에 대한 연결을 확인해주세요. 빈 앵커로 교체했을 때 활성 경로가 지워지는지도 포함해주세요. |
| 선택 정책 변경 | 정지 중 유지된 선택을 해제하고 새 정책으로 재평가하는지 확인해주세요. 요청 진행 중 정책 변경은 요청 종료 후의 재평가까지 확인해주세요. 진행 중 스크롤 처리 옵션 변경은 이후 요청에 적용되는 계약과 구분해주세요. |
| 성공 이후 선택 | `KeepRequestedUntilPositionChanges`에서는 성공한 요청 경로를 유지하다가 첫 보이는 인덱스 또는 오프셋 변경 후 다시 선택하는지 확인해주세요. `SelectionAfterScroll.FollowViewport`에서는 종료 시 경계 정책으로 재선택하는지 확인해주세요. |
| 취소·폐기 이후 선택 | 완료되지 않은 요청의 경로를 유지하지 않고 현재 뷰포트로 선택이 복귀하는지 확인해주세요. |
| 실제 아이템 매핑 | 전체 폭 헤더도 아이템 1개로 세어 앵커가 렌더링 순서와 일치하는지 확인해주세요. 아이템 인덱스와 행 번호를 혼동하지 마세요. |

레이아웃 완료 전 관측과 실제 완료를 구분하고, 프레임 시계·Compose 테스트 동기화로 검증해주세요. 임의의 긴 지연만으로 성공을 판정하지 마세요. UI 없는 선택 계산을 계측 테스트로만 검증하거나, 프레임 의존 동작을 JVM 테스트만으로 검증했다고 표현하지 마세요.

## 3. 현재 테스트 기반과 환경

태스크 선택 전에 [라이브러리 설정](../../../../section-sync/build.gradle.kts), [앱 설정](../../../../app/build.gradle.kts), [버전 카탈로그](../../../../gradle/libs.versions.toml), [Wrapper](../../../../gradle/wrapper/gradle-wrapper.properties)를 다시 확인해주세요. 아래는 하네스 도입 시점(2026-10-16)의 정적 확인이며, 이후 설정 변경의 원본은 각 파일입니다.

- 두 모듈에는 덧셈 단위 테스트와 패키지명 계측 테스트만 있습니다. 핵심 규칙·스크롤·취소·수명주기 회귀 테스트는 없습니다.
- `:app`에는 Compose UI 테스트와 테스트 manifest 의존성이 있습니다. `:section-sync`에는 JUnit·AndroidX JUnit·Espresso가 있고 Compose UI 테스트 의존성은 없습니다. `kotlinx-coroutines-test`도 선언되어 있지 않습니다. 필요하면 요청 범위와 맞춰 추가를 계획하고, 이미 있다고 가정하지 마세요.
- Wrapper는 Gradle 8.11.1, AGP는 8.10.1, 프로젝트 Kotlin은 2.0.21입니다. `--version`에 나오는 Gradle 내장 Kotlin 버전은 프로젝트 Kotlin 버전이 아닙니다.
- 두 모듈의 Java/Kotlin **컴파일 대상은 11**입니다. **Gradle 실행 JDK**는 AGP 8.10 기준 최소 17이며, 다른 JDK를 선택한다면 Wrapper 호환성도 확인해주세요. [AGP 호환성](https://developer.android.com/build/releases/agp-8-10-0-release-notes), [Gradle 8.11.1 호환성](https://docs.gradle.org/8.11.1/userguide/compatibility.html)을 참고해주세요.
- 두 모듈의 `compileSdk`는 37이고 `minSdk`는 21입니다. 로컬 SDK 설치·AGP 지원 범위는 별도로 확인해야 하며, 설정값이 있다고 환경 호환성이 검증된 것은 아닙니다.

## 4. 명령 선택과 결과 기록

저장소 루트의 Windows PowerShell 기준입니다. 다른 셸에서는 저장소의 `./gradlew`를 사용해주세요. **아래는 명령 후보이며 실행·성공 기록이 아닙니다.** 코드/설정 검증에서 환경이나 태스크가 확인되지 않았다면 먼저 진입점 명령으로 확인해주세요. 목록 조회가 실패하면 태스크 존재 여부는 미확인으로 남겨주세요.

| 적용 범위 | 명령 후보 |
| --- | --- |
| 실행 Gradle/JDK 확인 | `.\gradlew.bat --version` |
| 실제 태스크 목록 확인 | `.\gradlew.bat tasks --all` |
| 라이브러리 변경 | `.\gradlew.bat :section-sync:testDebugUnitTest :section-sync:lintDebug` |
| 샘플·공개 API 호출부 영향 | `.\gradlew.bat :app:testDebugUnitTest :app:lintDebug :app:assembleDebug` |
| 샘플의 프레임·제스처·수명주기 | `.\gradlew.bat :app:connectedDebugAndroidTest` |

라이브러리에 계측 테스트를 두는 작업이라면 의존성과 `:section-sync:connectedDebugAndroidTest` 태스크를 먼저 확인해주세요. 테스트가 없는 시나리오는 태스크 성공만으로 검증되지 않습니다. API 검토만 요청되었으면 호환성·호출부 정적 검토 결과와 실행하지 못한 검증을 구분해주세요.

문서·스킬만 변경하면 구조·링크·frontmatter·diff와 dry run을 우선하고, README 오탈자만으로 Gradle 조회나 전체 빌드를 실행하지 마세요. Markdown 안의 API 사용 예시나 기본값을 바꾸었다면 관련 공개 API와 다시 대조해주세요. Maven 게시·릴리스·서명 태스크는 기본 검증에서 제외해주세요.

실행 기록에는 명령, 실행 환경, 종료 상태, 실제 검증한 내용, 미실행 사유를 적어주세요. 권한·JDK/SDK·네트워크/로컬 통신 실패는 환경 오류로, 컴파일·테스트 assertion 실패는 해당 근거와 함께 코드/테스트 실패로 구분해주세요. 원인이 불명확하면 분류를 추정으로 남겨주세요.

계측 테스트 전 `adb devices -l` 등으로 기기 준비 상태를 확인해주세요. 기기가 없으면 미실행, ADB 조회 자체가 실패하면 **기기 유무 미확인·계측 미실행**으로 적어주세요. 기기/도구가 없더라도 가능한 정적 검토를 진행하고, 필수 통합 검증이 남는 제품 변경은 `partial`로 인계해주세요. 기본 예제 통과와 핵심 회귀 시나리오 통과는 별도로 기록해주세요.
