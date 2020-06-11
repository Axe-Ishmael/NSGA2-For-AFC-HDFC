package newFunction;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Front {

    // 存储所有在前沿中的Individual个体
    List<Individual> front;

    Front() {
        front = new ArrayList<Individual>();
    }

    // 添加新的individual到前沿中
    public void add(Individual individual) {
        this.front.add(individual);
    }

    // 返回在Front中的individual数量
    public int size() {
        return this.front.size();
    }

    // 返回整个前沿List
    public List<Individual> getIndividuals() {
        return this.front;
    }

    // 返回并删除前沿中的第一个individual
    public Individual pop() {
        return this.front.remove(0);
    }

    // Sets the ranks of all individuals in the front
    //为front中的每个individual设置rank
    //将front(ith)中的个体进一步进行层次划分，将划分出的个体加入到(i+1)th frontQ中,对应的rank为i+1
    public void setIndividualRanks(int frontCounter, Front frontQ) {
        front.forEach((p) -> {
            p.getDominatedIndividuals().forEach((q) -> {//对被p支配的所有个体进行遍历
                // Decrement the domination count for individual q
                q.decrementDominating();//??? 为何要减少一个支配q的individual的数量

                // If q is not dominated by any individuals then it belongs
                // to
                // front Q
                //如果支配q的数量为0，即p不被任何个体支配
                //则将q加入到front Q中
                if (q.getDominating() == 0) {
                    q.setRank(frontCounter + 1);
                    frontQ.add(q);
                }
            });
        });
    }


    // Assigns the crowding distance of each individual in the front which is
    // gives the euclidian distance between each individual based on their m
    // objectives in m dimensional space (for this problem m = 2)
	/*
	为前沿中每个个体分配crowding distance，crowding distance是基于欧几里得几何距离
	对每一个指标计算公式都要进行判断
	 */
    public void assignCrowdingDistance(Population population) {

        Collections.sort(this.front, new Comparator<Individual>() {
            @Override
            public int compare(Individual individual, Individual t1) {
                return Double.compare(individual.getAFC(),t1.getAFC());
            }
        });

        // Assign a large distance to the boundary solutions
        //给front头尾的individual设置一个较大的distance
        front.get(0).setCrowdingDistance(Double.MAX_VALUE);
        front.get(front.size() - 1).setCrowdingDistance(Double.MAX_VALUE);

        for (int k = 1; k < front.size() - 1; k++) {
            double crowdingDistance = front.get(k).getCrowdingDistance();//获得当前第k个individual的CrowdingDistance

            double nextIndividualValue = front.get(k + 1).getAFC();
            double lastIndividualValue = front.get(k - 1).getAFC();

            double valueDifference = population.getMaxAFC() //population)中的最大最小值之差
                    - population.getLeastAFC();

            crowdingDistance += (nextIndividualValue - lastIndividualValue)//计算crowdingDistance
                    / valueDifference;
            front.get(k).setCrowdingDistance(crowdingDistance);//设置crowdingDistance
        }

        Collections.sort(this.front, new Comparator<Individual>() {
            @Override
            public int compare(Individual individual, Individual t1) {
                return Double.compare(individual.getHDFT(),t1.getHDFT());
            }
        });

        // Assign a large distance to the boundary solutions
        //给front头尾的individual设置一个较大的distance
        front.get(0).setCrowdingDistance(Double.MAX_VALUE);
        front.get(front.size() - 1).setCrowdingDistance(Double.MAX_VALUE);

        for (int k = 1; k < front.size() - 1; k++) {
            double crowdingDistance = front.get(k).getCrowdingDistance();//获得当前第k个individual的CrowdingDistance

            double nextIndividualValue = front.get(k + 1).getHDFT();
            double lastIndividualValue = front.get(k - 1).getHDFT();

            double valueDifference = population.getMaxHDFT() //population)中的最大最小值之差
                    - population.getLeastHDFT();

            crowdingDistance += (nextIndividualValue - lastIndividualValue)//计算crowdingDistance
                    / valueDifference;
            front.get(k).setCrowdingDistance(crowdingDistance);//设置crowdingDistance
        }

    }


}
