package main.calculadora;

import java.io.*;


/**
 *
 * Classe para gerenciado de arquivos
 */
public class FileManager {

    /**
     * Criar arquivo TXT em branco
     * @param label
     * @return
     * @throws IOException
     */
    public static File createFile(String label) throws IOException {
        File f = new File("/Users/Caio/Documents/Workspace/Paralelismo Iterativo/TXT's/" + label + ".txt");
        FileWriter writer = new FileWriter(f);
        writer.write("");
        writer.close();
        return f;
    }

    /**
     * Função para escrever a matrix dentro do txt criado
     * @param matrix
     * @param label
     * @throws IOException
     */
    public static void writeMatrix(int[][] matrix, String label) throws IOException {
        File file;
        file = createFile(label);

        int lines, columns;
        lines = matrix.length;
        columns = matrix[0].length;

        FileWriter writer = new FileWriter(file);
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                if (i == lines - 1 && j == columns - 1) {
                    writer.write(matrix[i][j] + "");
                } else
                    writer.write(matrix[i][j] + ",");
                if (j == (columns - 1)) {
                    writer.write("\n");
                }
            }
        }
        writer.close();
    }

    /**
     * Ler matriz do arquivo e passar para variavel matrixR
     * @param matrixR
     * @param label
     * @throws IOException
     */
    public static void readMatrix(int[][] matrixR, String label) throws IOException {
        File f = new File("/Users/Caio/Documents/Workspace/Paralelismo Iterativo/TXT's/" + label + ".txt");
        FileReader fr = new FileReader(f);
//1000 x 2000
        try{
            BufferedReader br = new BufferedReader(fr);
            String strLine;
            int counter = 0;

            while ((strLine = br.readLine()) != null)   {
                try{
                    String [] noInStringArr = strLine.split(",");
                    int [] n = new int [matrixR[0].length];

                    for(int k = 0; k < n.length; k++){
                        n[k] = Integer.parseInt(noInStringArr[k]);
                    }

                        for (int j = 0; j < matrixR[0].length; j++) {
                            matrixR[counter][j] = n[j];
                        }
                        counter++;

                }catch(NumberFormatException nfe){
                    System.err.println("Error: " + nfe.getMessage());
                }

            }
            br.close();
        }catch (Exception e){
            System.err.println("Error Exception:" + label + e.getMessage());
        }finally{
            fr.close();
        }

    }
}
