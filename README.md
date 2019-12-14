# PdfUtil
A java application that combines pdf tools using an intuitive drag-and-drop interface.

## NOTE
The project was migrated from the NetBeans IDE to the IntelliJ IDE.
Gradle (with the Kotlin DSL) is used as build manager.

## Current features
 - Merging
 - File information (number of pages, size, author, creator, title, subject)
 - Password protection and password stripping

![](pdf_util.png)

## Future features
- PDF splitting
- Image extraction
- Text extraction
- Watermarking

## Used libraries
- Apache PDFBox 2.0.16 & preflight 2.0.16 (https://pdfbox.apache.org)
- FileDrop (http://iharder.sourceforge.net/current/java/filedrop/)

## Usage
! DEPRECATED SINCE MIGRATION FROM NETBEANS TO INTELLIJ
```
java -jar ./dist/PdfUtil.jar
```
