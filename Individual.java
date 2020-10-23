import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Individual {

    Demand demand[];
    int numMedian;
    int fitness;
    int median[];
    int P;
    int itr;



    public Individual(String[] str_row,int P,int itr) throws Exception{

        this.demand = get_demand_distance(str_row);
        this.P = P;
        this.median = new int[P];
        this.itr = itr;

    }


    public void repair_median(){

        int count = 0;
        ArrayList<Integer> m = new ArrayList<Integer>();


        for(int i=0;i<demand.length;i++){
            if(demand[i].isMedian){
                count++;
                m.add(i);
            }
        }

        if(count>P){
            while(count>P){
                int r = GeneticAlgorithm.rand.nextInt(count);
                demand[m.get(r)].isMedian = false;

                count--;
                m.remove(GeneticAlgorithm.rand.nextInt(count));
            }
        }
        if(count<P){
            while(count<P){
                int r = GeneticAlgorithm.rand.nextInt(demand.length);
                if(!m.contains(r)){
                    demand[r].isMedian = true;
                    count++;
                    m.add(r);
                }

            }
        }

        median = new int[P];

        for(int i=0;i<m.size();i++){
            median[i] = m.get(i);
        }

    }




    public Demand [] get_demand_distance(String str_row[]) throws Exception{

        Demand[] temp = new Demand[str_row.length-1];

        for(int i = 1;i < str_row.length;i++){
            temp[i-1] = new Demand(str_row[i]);
        }

        return temp;

    }



    public void gen_rand_median(Random rand, int P){

        int count = 0;
        int max_rand = demand.length;


        //random set median until count = P
        while(count<P){

            int index = rand.nextInt(max_rand);
            if(!demand[index].isMedian){
                demand[index].isMedian = true;
                median[count] = index;
                count++;
            }

        }

        this.numMedian = count;

    }

    public void gen_fix_median(){

        this.median = new int[5];
        demand[6].isMedian = true;
        median[0] = 6;
        demand[12].isMedian = true;
        median[1] = 12;
        demand[64].isMedian = true;
        median[2] = 64;
        demand[90].isMedian = true;
        median[3] = 90;
        demand[98].isMedian = true;
        median[4] = 98;


    }





    public void assign_demand_to_median(){

        //Assign all demand points to nearest median
        for(int i=0;i<demand.length;i++){

            int index_median = 0;
            int nearest = Integer.MAX_VALUE;

            for(int j = 0;j<median.length;j++){
                int distance = demand[i].distance[median[j]];

                if(distance<nearest){

                    index_median = j;

                    nearest = distance;

                }

            }

            demand[i].assigned = median[index_median];
//            System.out.println(demand[i].assigned);

        }

    }

    public void fitness_calculation(){



        fitness = 0;

        for(int i = 0;i < median.length;i++){

            int sum_dis = 0;
            int min = Integer.MAX_VALUE;
            int temp_median = -1;
//            System.out.println("median "+median[i]);
            for(int j = 0;j < demand.length;j++){
//                System.out.println("demand by"+demand[j].assigned);
                int distance = 0;

                if(demand[j].assigned  == median[i]){

                    for(int k = 0;k < demand.length;k++){
                        if(demand[k].assigned == median[i]){
                            distance += demand[j].distance[k];
                        }
                    }


                    if(distance < min){
                        min = distance;
                        temp_median = j;
                    }

                }


            }



            int old_median = median[i];

            demand[old_median].isMedian = false;
            median[i] = temp_median;
            demand[median[i]].isMedian = true;

            for(int j=0;j<demand.length;j++){
                if(demand[j].assigned == old_median)
                    demand[j].assigned = median[i];
            }

            for(int j=0;j<demand.length;j++){
                //from equation sum of min distance P-median
                if(demand[j].assigned == median[i])
                    sum_dis += demand[j].distance[median[i]];
            }

//            System.out.println("fitness from "+median[i]+"have "+sum_dis);
            fitness += sum_dis;

        }

        //Check for best solution
        if(fitness<GeneticAlgorithm.best_distance){
            GeneticAlgorithm.best_distance = fitness;
            GeneticAlgorithm.best_solution = this;
            GeneticAlgorithm.best_itr = itr;
        }

    }

    public int get_fitness(){
        return this.fitness;
    }


    public String toString(){
        StringBuilder sb = new StringBuilder(median.length);
        sb.append(" [ ");
        for(int i : median){
            sb.append(i+", ");
        }
        sb.append(" ] Fitness : "+get_fitness());
        return sb.toString();
    }


}
