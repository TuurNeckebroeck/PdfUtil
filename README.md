# PdfUtil
A Java application that combines pdf tools using an intuitive drag-and-drop interface.

## Current features
 - Loading multiple pdf's into workspace
 - Merging
 - Splitting
 - Show pdf information (number of pages, size, author, creator, title, subject)
 - Recto-verso printing for 1-sided printers
 - Password protection and password stripping

![](pdf_util.png)

## Future features
- Image extraction (WIP)
- Text extraction (WIP)
- Watermarking (WIP)
- Watermark removing

## Used libraries
- Apache PDFBox 2.0.16 & preflight 2.0.16 (https://pdfbox.apache.org)
- FileDrop (http://iharder.sourceforge.net/current/java/filedrop/)


## Releases
Releases can be found at http://neckebroecktuur.ulyssis.be/pdfutil/releases

## Usage
Release 2.0:
```
java -jar PdfUtil-2.0-SNAPSHOT.jar
```

After release 2.0 (unstable)
```
java -jar PdfUtil-2.0-SNAPSHOT.jar --interface gui
```