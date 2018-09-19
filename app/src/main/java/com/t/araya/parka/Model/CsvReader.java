package com.t.araya.parka.Model;

import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    private List<String[]> rows;

    public CsvReader() {
        rows = new ArrayList<>();
    }

    public List<String[]> readCSV(String fileName) throws IOException {
        String fileDirectory = Environment.getExternalStorageDirectory() + "/_Parka/AccelerometerCsvFile";
        File fileToGet = new File(fileDirectory, fileName);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToGet));
        String line;
        String csvSplitBy = ",";

        bufferedReader.readLine();

        while ((line = bufferedReader.readLine()) != null) {
            String[] row = line.split(csvSplitBy);
            rows.add(row);
//            Log.i("row size in CsvReader", "---------------> size = " + rows.size() + "");
        }

        bufferedReader.close();

        return rows;
    }

    public List<String[]> getRows(){
        return rows;
    }

}
