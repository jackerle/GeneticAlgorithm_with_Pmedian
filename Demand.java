import java.util.Random;

public class  Demand{

        boolean isMedian;
        int[] distance;
        int assigned;

public Demand(String v ){

        assigned = -1;
        isMedian = false;
        String[] _temp = v.split(" ");
        distance = new int[_temp.length];
//        isStation = (rand.nextDouble() < 0.5)? 0 : 1;
        for(int i=0;i<_temp.length;i++){
        distance[i] = Integer.parseInt(_temp[i]);
        }

        }

@Override
public String toString() {
        return "Vertex{" +
        "isStation=" + isMedian +
        ", distance='" + distance + '\'' +
        '}';
        }
        }
