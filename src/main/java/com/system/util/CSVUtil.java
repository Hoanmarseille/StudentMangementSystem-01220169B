package com.system.util;

import com.system.domain.Student;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

    /**
     * Requirement 5.2: Export student data to CSV
     */
    public static void exportToCSV(List<Student> students, String filePath) throws Exception {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            // Write Header
            writer.println("Student ID,Full Name,Programme,Level,GPA,Status");

            for (Student s : students) {
                writer.printf("%s,%s,%s,%d,%.2f,%s%n",
                        s.getStudentId(),
                        s.getFullName(),
                        s.getProgramme(),
                        s.getLevel(),
                        s.getGpa(),
                        s.getStatus()
                );
            }
        }
    }

    /**
     * Requirement 5.2: Import students from a CSV file
     */
    public static List<Student> importFromCSV(String filePath) throws Exception {
        List<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                // Skip the first line (ID, Name, etc.) to avoid NumberFormatExceptions
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Split by comma
                String[] data = line.split(",");

                // Ensure the row has enough columns before processing
                if (data.length >= 6) {
                    Student s = new Student();
                    s.setStudentId(data[0].trim());
                    s.setFullName(data[1].trim());
                    s.setProgramme(data[2].trim());
                    s.setLevel(Integer.parseInt(data[3].trim()));
                    s.setGpa(Double.parseDouble(data[4].trim()));
                    s.setStatus(data[5].trim());

                    // Set default values for fields usually not in basic CSVs
                    s.setDateAdded(LocalDateTime.now());

                    students.add(s);
                }
            }
        }
        return students;
    }
}