package com.soarclient.libraries.resourcepack.pack;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.soarclient.libraries.resourcepack.Util;

public class ZipPack extends Pack {

    public ZipPack(Path path) {
        super(path);
    }

    @Override
    public ZipPack.Handler createHandler() {
        return new ZipPack.Handler(this);
    }

    @Override
    public String getFileName() {
        return path.getFileName().toString().substring(0, path.getFileName().toString().length() - 4);
    }

    public static class Handler extends Pack.Handler {

        public Handler(Pack pack) {
            super(pack);
        }

        public Path getConvertedZipPath() {
            return pack.getWorkingPath().getParent().resolve(pack.getWorkingPath().getFileName() + ".zip");
        }

        @Override
        public void setup() throws IOException {
            if (pack.getWorkingPath().toFile().exists()) {
                System.out.println("  Deleting existing conversion");
                Util.deleteDirectoryAndContents(pack.getWorkingPath());
            }

            Path convertedZipPath = getConvertedZipPath();
            if (convertedZipPath.toFile().exists()) {
                System.out.println("  Deleting existing conversion zip");
                convertedZipPath.toFile().delete();
            }

            pack.getWorkingPath().toFile().mkdir();

            try {
                extractZip(pack.getOriginalPath(), pack.getWorkingPath());
            } catch (Exception e) {
                Util.propagate(e);
            }
        }

        private void extractZip(Path zipFilePath, Path targetDirectory) throws IOException {
            try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath.toFile()))) {
                ZipEntry entry;
                while ((entry = zipIn.getNextEntry()) != null) {
                    Path filePath = targetDirectory.resolve(entry.getName());
                    
                    // Create directories if needed
                    if (entry.isDirectory()) {
                        Files.createDirectories(filePath);
                    } else {
                        // Ensure parent directory exists
                        Files.createDirectories(filePath.getParent());
                        
                        // Extract file
                        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath.toFile()))) {
                            byte[] buffer = new byte[8192];
                            int read;
                            while ((read = zipIn.read(buffer)) != -1) {
                                bos.write(buffer, 0, read);
                            }
                        }
                    }
                    zipIn.closeEntry();
                }
            }
        }

        @Override
        public void finish() throws IOException {
            try {
                System.out.println("  Zipping working directory");
                zipDirectory(pack.getWorkingPath(), getConvertedZipPath());
            } catch (Exception e) {
                Util.propagate(e);
            }

            System.out.println("  Deleting working directory");
            Util.deleteDirectoryAndContents(pack.getWorkingPath());
        }

        private void zipDirectory(Path sourceDirectory, Path zipFilePath) throws IOException {
            // Make sure the parent directory exists
            Files.createDirectories(zipFilePath.getParent());

            try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(
                    new FileOutputStream(zipFilePath.toFile())))) {
                
                Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        // Create a relative path to maintain directory structure
                        Path relativePath = sourceDirectory.relativize(file);
                        String entryName = relativePath.toString().replace(File.separatorChar, '/');
                        
                        ZipEntry zipEntry = new ZipEntry(entryName);
                        zipOut.putNextEntry(zipEntry);
                        
                        Files.copy(file, zipOut);
                        zipOut.closeEntry();
                        
                        return FileVisitResult.CONTINUE;
                    }
                    
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        if (!dir.equals(sourceDirectory)) {
                            Path relativePath = sourceDirectory.relativize(dir);
                            String entryName = relativePath.toString().replace(File.separatorChar, '/') + '/';
                            
                            zipOut.putNextEntry(new ZipEntry(entryName));
                            zipOut.closeEntry();
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
                
                // Flush with a large buffer size (similar to the original 65536 value)
                zipOut.flush();
            }
        }

        @Override
        public String toString() {
            return "Handler{} " + super.toString();
        }
    }
}