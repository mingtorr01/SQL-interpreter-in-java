# SQL-interpreter-in-java

제목 : SQL 처리기 개발  

내용 : 임의의 SQL문을 문자열로 입력하여 SQL문의 실행 결과를 출력하는 프로그램  

언어 : 자바

구현 방법 : 

1. 8종류 관계연산자를 별도로 구현

2. 임의의 SQL문을 단일 문자열로 입력 

   예: "SELECT sno, sname FROM student WHERE sno = 100;" => 문자열로 입력

3. SQL문 처리에 필요한 관계연산자를 순서대로 호출하여 검색 결과 출력

   (1) CALL RESTRICT(student, sno, 100) => studenet 테이블에서 sno=100 튜플 출력

   (2) CALL PROJECT( (1), sno, sname) => (1)번 결과에서 sno, sname 출력 
