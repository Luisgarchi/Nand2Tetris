/*
 * USAGE
 * 
 * 
 * Input:
 *  - A single file ('fileName.jack')
 *  - A name of a directory containing one or more .jack files ('directoryName')
 * 
 * Output:
 *  - If the input is a single file: fileName.xml
 *  - If the input is a directory: one .xml file for every .jack file
 */

import java.io.File;
import java.io.FilenameFilter;

public class JackAnalyzer{


    public static void main(String[] args){
        String input = args[0];

        // Check if the input is a single Jack file or a directory
        String inputType = (input.endsWith(".jack")) ? "FILE" : "DIRECTORY";

        // List files to compile
        File[] files;
        if(inputType.equals("FILE")){
            files = new File[] {
                new File(input)
            };
        }
        else {
            File f = new File(input);
            files = f.listFiles(new JackFileFilter());
        }


        


        for(int i = 0; i < files.length; i++){

            JackTokenizer tokenizer = new JackTokenizer(files[i]);

            
        }
    }

}


class JackFileFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return name.endsWith(".jack");
    }
}