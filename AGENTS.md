# ComposeSectionSync 작업 안내

Jetpack Compose의 탭 선택과 섹션 스크롤을 양방향으로 동기화하는 베타 라이브러리입니다. 사용 계약과 지원 범위는 [README](README.md)를 기준으로 확인해주세요. 현재 지원 대상은 `LazyVerticalGrid`입니다.

## 책임과 의존 방향

- `:section-sync`는 배포 라이브러리이고, `:app`은 이를 사용하는 중첩 카테고리 샘플입니다. 라이브러리가 샘플에 의존하지 않도록 해주세요.
- `SectionPath`, `SectionAnchor`, `SectionResolver`는 경로·앵커 제약·순수 선택 규칙을 담당합니다. 여기에 UI, 코루틴, 수명주기 책임을 넣지 마세요.
- `SectionSyncState`와 `RememberSectionSyncState`는 `LazyGridState`, 요청 취소, Compose 상태 관찰과 수명주기를 연결합니다. 샘플은 표시 데이터, 실제 아이템 인덱스 매핑, 탭·그리드 UI를 담당합니다.
- Clean Architecture는 이 책임과 의존 방향을 유지하는 데 적용해주세요. 별도 domain/data 모듈, Repository, UseCase, DI 프레임워크는 실제 필요가 입증될 때만 제안해주세요.

## 공통 작업 규칙

- 응답은 사용자가 요청한 언어를 우선하고, 별도 요청이 없으면 현재 대화의 언어를 따라주세요. 한국어로 응답할 때는 존댓말을 사용해주세요.
- 코드와 테스트 예시는 특별한 이유가 없다면 Kotlin으로 작성해주세요.
- 시작 전에 “이 저장소에서 반복되는 실패를 줄이는 가장 작은 구성은 무엇인가?”를 검토하고, 답을 작업 계획에 반영해주세요.
- `git status --short`, `git diff`, `git diff --cached`와 적용되는 하위 `AGENTS.md`를 먼저 확인하고 기존 사용자 변경을 보존해주세요.
- 라이브러리 개발·버그 수정·공개 API 검토·회귀 검증에는 [section-sync-workflow](.agents/skills/section-sync-workflow/SKILL.md)를 사용해주세요. README 오탈자처럼 동작과 무관한 수정은 직접 처리해주세요.
- 공개 API·기본 동작 변경의 호출부·호환성·문서 검토는 위 워크플로를 따르고, PR 작성 시 [기존 템플릿](.github/pull_request_template.md)을 재사용해주세요.

## 검증 진입점

제품 코드나 빌드 설정을 다룰 때 저장소 루트에서 `./gradlew --version` 또는 Windows PowerShell의 `.\gradlew.bat --version`으로 실행 환경을 확인해주세요. Java/Kotlin 컴파일 대상 11과 Gradle 실행 JDK 요구사항은 다릅니다.

변경별 태스크 선택, 실행 JDK, 현재 테스트 기반, 위험 시나리오는 [검증 지침](.agents/skills/section-sync-workflow/references/validation.md)을 참고해주세요. 문서·스킬만 수정할 때는 구조·링크·시나리오 검증을 우선하고, 같은 체크아웃이나 기기를 쓰는 Gradle 실행은 직렬화해주세요.
