import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GeneticAlgorithm {

    static int popsize = 1000;   //input
    static int seed = 28;       //input
    static int P;
    static int maxgen = 1000;   //input
    static int tournament_size = 20;   //input
    static double crossoverrate = 0.8;  //input
    static double mutationrate = 0.02;  //input
    static int itr = 0;
    static String path = "F:\\code\\SpringbootWS\\PMedian\\src\\instance\\pmed1.in";
    static int best_distance = Integer.MAX_VALUE;
    static int best_itr;
    static Individual best_solution = null;
    static Random rand = new Random((long)seed);


    public static boolean GA_cal(JLabel itr_gui,JLabel best_gui)throws Exception{

        popsize = Controller.popsize;
        seed = Controller.seed;
        maxgen = Controller.maxgen;
        tournament_size = Controller.tournament_size;
        crossoverrate = Controller.crossoverrate;
        mutationrate = Controller.mutationrate;
        path = Controller.path;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(itr<maxgen){
                    try{
                        itr_gui.setText("Itr:"+(itr+1)+"/"+maxgen);
                        best_gui.setText("Best:"+best_distance + " at itr : "+best_itr);
                        itr_gui.repaint();
                        best_gui.repaint();
                        itr_gui.paintImmediately(itr_gui.getVisibleRect());
                        best_gui.paintImmediately(best_gui.getVisibleRect());
                        Thread.sleep(10);
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }
                }
            }
        };


        //get String from file
        String[] str = get_string_file_from_path(path);
        P = Integer.parseInt(str[0].split(" ")[2]);



        //Init Population
        Individual population[] = new Individual[popsize];
        for(int i=0;i<popsize;i++) {

            //create population
            population[i] = new Individual(str,P,0);

            //Randomly select p-medians
            population[i].gen_rand_median(rand,P);
//            population[i].gen_fix_median();

            //Assign all demand points to nearest median
            population[i].assign_demand_to_median();

            //Set Center point and Fitness_calculation
            population[i].fitness_calculation();
//            System.out.println(population[i]+"Best solution is "+best_solution);

        }

        System.out.println("Population created with : "+popsize+" Maxgen : "+maxgen);
        System.out.println("Best Solution is : "+best_solution);
        System.out.println("----------------------------------------------");
        Thread.sleep(1000);
        System.out.println("Start GA with "+maxgen+" time.");

        //Do task Until maxgen
        new Thread(runnable).start();
        for(;itr<maxgen;itr++){

            //Create Offspring
            Individual[] offspring = new Individual[popsize];

            //Tournament selection
            for(int i = 0;i<popsize;i++){

                int champion = rand.nextInt(popsize);
                int contender[] = new int[tournament_size];

                for(int e : contender)
                    e = rand.nextInt();

                for(int k = 0;k < contender.length;k++){
                    if(population[champion].get_fitness()>population[contender[k]].get_fitness())
                        champion = contender[k];
                }

//                System.out.println(offspring.length);
                offspring[i] = new Individual(str,P,itr);
                offspring[i].median = population[champion].median;
                offspring[i].demand = population[champion].demand;

            }

            //Cross Overr
            for(int i = 0;i<popsize-1;i=i+2){
                if(rand.nextDouble()<crossoverrate){
                    int splitPoint = rand.nextInt(offspring[i].demand.length);
                    for(int j = splitPoint;j<offspring[i].demand.length;j++){
                        Demand temp = offspring[i].demand[j];
                        offspring[i].demand[j] = offspring[i+1].demand[j];
                        offspring[i+1].demand[j] = temp;
                    }

                    offspring[i].repair_median();
                    offspring[i].assign_demand_to_median();
                    offspring[i+1].repair_median();
                    offspring[i+1].assign_demand_to_median();
                }
            }

            //Mutation
            for(int i = 0;i<popsize;i++){
                if(rand.nextDouble() < mutationrate){
                    int mutationPoint  = rand.nextInt(offspring[i].demand.length);

                    //toggle if mutate
                    if(offspring[i].demand[mutationPoint].isMedian)
                        offspring[i].demand[mutationPoint].isMedian = false;
                    else
                        offspring[i].demand[mutationPoint].isMedian = true;

                    offspring[i].repair_median();
                }
            }

            //fitness calculate for offspring
            for(int i = 0;i<popsize;i++){
                offspring[i].assign_demand_to_median();
                offspring[i].fitness_calculation();
            }

            //Copy offspring to nextgeneration
            population = offspring;



            System.out.println("Lap "+itr+"/"+maxgen+" Best Solution is : "+best_solution);
        }
        return true;

    }


    public static void main(String[] args)throws Exception{

        //get String from file
        String[] str = get_string_file_from_path(path);
        P = Integer.parseInt(str[0].split(" ")[2]);



        //Init Population
        Individual population[] = new Individual[popsize];
        for(int i=0;i<popsize;i++) {

            //create population
            population[i] = new Individual(str,P,0);

            //Randomly select p-medians
            population[i].gen_rand_median(rand,P);
//            population[i].gen_fix_median();

            //Assign all demand points to nearest median
            population[i].assign_demand_to_median();

            //Set Center point and Fitness_calculation
            population[i].fitness_calculation();
//            System.out.println(population[i]+"Best solution is "+best_solution);

        }

        System.out.println("Population created with : "+popsize+" Maxgen : "+maxgen);
        System.out.println("Best Solution is : "+best_solution);
        System.out.println("----------------------------------------------");
        Thread.sleep(1000);
        System.out.println("Start GA with "+maxgen+" time.");

        //Do task Until maxgen
        for(int itr =0;itr<maxgen;itr++){

            //Create Offspring
            Individual[] offspring = new Individual[popsize];

            //Tournament selection
            for(int i = 0;i<popsize;i++){

                int champion = rand.nextInt(popsize);
                int contender[] = new int[tournament_size];

                for(int e : contender)
                    e = rand.nextInt();

                for(int k = 0;k < contender.length;k++){
                    if(population[champion].get_fitness()>population[contender[k]].get_fitness())
                        champion = contender[k];
                }

//                System.out.println(offspring.length);
                offspring[i] = new Individual(str,P,itr);
                offspring[i].median = population[champion].median;
                offspring[i].demand = population[champion].demand;

            }

            //Cross Overr
            for(int i = 0;i<popsize-1;i=i+2){
                if(rand.nextDouble()<crossoverrate){
                    int splitPoint = rand.nextInt(offspring[i].demand.length);
                    for(int j = splitPoint;j<offspring[i].demand.length;j++){
                        Demand temp = offspring[i].demand[j];
                        offspring[i].demand[j] = offspring[i+1].demand[j];
                        offspring[i+1].demand[j] = temp;
                    }

                    offspring[i].repair_median();
                    offspring[i].assign_demand_to_median();
                    offspring[i+1].repair_median();
                    offspring[i+1].assign_demand_to_median();
                }
            }

            //Mutation
            for(int i = 0;i<popsize;i++){
                if(rand.nextDouble() < mutationrate){
                    int mutationPoint  = rand.nextInt(offspring[i].demand.length);

                    //toggle if mutate
                    if(offspring[i].demand[mutationPoint].isMedian)
                        offspring[i].demand[mutationPoint].isMedian = false;
                    else
                        offspring[i].demand[mutationPoint].isMedian = true;

                    offspring[i].repair_median();
                }
            }

            //fitness calculate for offspring
            for(int i = 0;i<popsize;i++){
                offspring[i].assign_demand_to_median();
                offspring[i].fitness_calculation();
            }

            //Copy offspring to nextgeneration
            population = offspring;



            System.out.println("Lap "+itr+"/"+maxgen+" Best Solution is : "+best_solution);
            Thread.sleep(500);
        }




    }

    static public String[] get_string_file_from_path(String path) throws Exception{
        Scanner file = new Scanner(new File(path),"UTF-8");
        String _text = file.useDelimiter("\\A").next();
        String[] text = _text.split("\n");
        return  text;
    }

}
