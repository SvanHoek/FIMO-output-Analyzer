package TFBS_finder;

import java.util.ArrayList;
import java.util.HashMap;

public class Find_TFBS {
    private ArrayList<int[]> gene_index = new ArrayList<>();
    private ArrayList<int[]> gene_compliment_index = new ArrayList<>();
    private ArrayList<String[]> TFBS_info = new ArrayList<>();
    private HashMap<String, String> gene_name = new HashMap<>();
    public String go(String FIMO_text, String GenBank_text){
        StringBuilder TFBS = new StringBuilder();
        this.get_gene_index(GenBank_text);
        this.get_gene_name(GenBank_text);
        String[] FIMO_array = FIMO_text.split("\n");
        String[] temp_array;
        for (int i = 1 ; i < FIMO_array.length ; i++) {
            temp_array = FIMO_array[i].substring(14).split("\t");
            if(this.not_in_gene(temp_array)){
                this.close_to_gene(temp_array);
            }
        }
        for(String[] line : TFBS_info){
            TFBS.append(line[0]).append("\t").append(line[1]).append("\t").append(line[6]).append("\t").append(line[7])
                    .append("\t").append(line[8]).append("\t").append(line[9]).append("\n");
        }
        return TFBS.toString();
    }

    private void get_gene_index(String GenBank_text){
        String[] GenBank_array = GenBank_text.split("\n");
        String temp_string;
        int[] temp_array = new int[2];
        for(String line : GenBank_array) {
            if(line.startsWith("     gene")){
                temp_string = line.substring(21).replace(")", "");
                if(temp_string.startsWith("c")){
                    for (int i = 0; i < 2; i++){
                        temp_array[i] = Integer.parseInt(temp_string.substring(11).split("\\.\\.")[i]);
                    }
                    gene_compliment_index.add(new int[]{temp_array[0], temp_array[1]});
                } else {
                    for (int i = 0; i < 2; i++){
                        temp_array[i] = Integer.parseInt(temp_string.split("\\.\\.")[i]);
                    }
                    gene_index.add(new int[]{temp_array[0], temp_array[1]});
                }
            }
        }
    }

    private void get_gene_name(String GenBank_text){
        String[] GenBank_array = GenBank_text.split(" {5}gene {12}");
        String start;
        String name;
        for (String gene : GenBank_array) {
            start = gene.replace("complement(", "").split("\\.\\.")[0];
            name = "not found";
            if (gene.split("\n")[1].startsWith("                     /gene=")){
                name = gene.replace("                     /gene=", "").replace("\"", "").split("\n")[1];
//                System.out.println(name);
            }
        gene_name.put(start, name);
        }

    }

    private boolean not_in_gene(String[] pos_TFBS){
        boolean in_gene = false;
        for(int[] indexes : gene_index){
            if(indexes[0] <= Integer.parseInt(pos_TFBS[0]) &&  indexes[1] >= Integer.parseInt(pos_TFBS[0]) ||
                    indexes[0] <= Integer.parseInt(pos_TFBS[1]) && indexes[1] >= Integer.parseInt(pos_TFBS[1])){
                in_gene = true;
//                System.out.println(indexes[0] + " " + indexes[1] + "|" + pos_TFBS[0] + " " + pos_TFBS[1]);
            }
        }
        for(int[] indexes : gene_compliment_index){
            if(indexes[0] <= Integer.parseInt(pos_TFBS[0]) &&  indexes[1] >= Integer.parseInt(pos_TFBS[0]) ||
                    indexes[0] <= Integer.parseInt(pos_TFBS[1]) && indexes[1] >= Integer.parseInt(pos_TFBS[1])){
                in_gene = true;
//                System.out.println(indexes[0] + " " + indexes[1] + "|" + pos_TFBS[0] + " " + pos_TFBS[1]);
            }
        }
        return !in_gene;
    }
    private void close_to_gene(String[] pos_TFBS){
        int distance = 0;
        String[] temp_array = new String[10];
        if(pos_TFBS[2].equals("+")){
            for(int[] indexes : gene_index){
                distance =  indexes[0] - Integer.parseInt(pos_TFBS[1]);
                add_TFBS_info(pos_TFBS, distance, temp_array, indexes);
            }
        } else {
            for(int[] indexes : gene_index){
                distance =  Integer.parseInt(pos_TFBS[0]) - indexes[1];
                add_TFBS_info(pos_TFBS, distance, temp_array, indexes);
            }
        }
    }

    private void add_TFBS_info(String[] pos_TFBS, int distance, String[] temp_array, int[] indexes) {
        if(distance < 300 && distance > 0 ){
            System.arraycopy(pos_TFBS, 0, temp_array, 0, 7);
            temp_array[7] = Integer.toString(indexes[0]);
            temp_array[8] = Integer.toString(indexes[1]);
            temp_array[9] = gene_name.get(Integer.toString(indexes[0]));
            TFBS_info.add(temp_array);
//            System.out.println(distance);
        }
    }
}
