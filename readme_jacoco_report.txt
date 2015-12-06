in Evento folder
check if all test are enable (no ignore)
create jacocoReport
    ./gradlew jacocoReport
if not executable
    chmod +x gradlew
open the report in web brownser
    open "app/build/reports/jacoco/jacocoReport/html/index.html"