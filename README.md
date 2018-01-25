# ancestry2html
Html export extension for Ancestry application (https://ancestry.nethar.cz/)

# Recommended IDE
JetBrains IntelliJ IDEA - https://www.jetbrains.com/idea/

# Building
You can use command line or IntelliJ IDEA:

## Command line
- Navigate to the working dir and run `mvn clean install package`
- Copy `Ancestry2html-xxxxx.jar` file from `target/` in root of project to `dist/Ancestry2html/lib/`
- Now you can run it from `dist/Ancestry2html/lib/`

## IntelliJ IDEA
- Open Ancestry2html project in IntelliJ IDEA and add Maven build configuration.
- Build it
- Copy `Ancestry2html-xxxxx.jar` file from `target/` in root of project to `dist/Ancestry2html/lib/`
- Now you can run it from `dist/Ancestry2html/lib/`
