별도 설치된 tomcat에 배포하기 위한 war 프로젝트.
JDK 1.8.0, Apache Maven 3.6.3, spring-tool-suite-4.11.0 환경에서 개발.

로그인 할 때 추가 필드 사용 지원. (userDomain 속성)
추가 필드 기능 활성화 하려면 USE_EXTRA_LOGIN_FILED 수정.
auth.extra 패키지와 SecurityConfig에서 추가 필드 지원 기능 구현.

USER_ROLE_MAP 테이블에 등록되지 않은 계정은 ROLE_USER 권한으로 로그인 됨.
권한 등록된 계정만 로그인 시키려면 UserRoleStrategy 수정.

메뉴 구조는 무한 depth 지원함.
메뉴 표시 샘플은 left.jsp에서 2depth 까지만 표시.
메뉴는 캐시로 관리하므로 DB에서 메뉴 변경 후 /api/menu/clearcache 호출 필요.

ROLE_MENU_MAP 테이블에 등록되지 않은 메뉴는 화면에 표시되지 않음.
권한이 없는 메뉴도 화면에 표시하고 접근제어 하려면 menu-mapper.xml(getMenuByRole) 수정.

ROLE_MENU_MAP 테이블 설정에 따라 메뉴와 메뉴 종속 API 접근제어 기능 지원. (대부분 불필요)
메뉴 접근제어만 하려면 UriSecurityAspect 활성화만 하면 됨.
메뉴 종속 API 접근제어도 하려면 UriSecurityAspect 활성화 후 컨트롤러에 @AccessMapping 추가.

apache Tiles3와 JSP 기반으로 화면 구성.
func 패키지는 대메뉴 기능을 샘플로 구현한 패키지.

war 파일 빌드 방법
  cd project_folder
  mvn clean package -DskipTests
