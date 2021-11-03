package ub.cse.algo;

import java.util.ArrayList;

public class Solution {

    private Info info;
    private Graph graph;
    private ArrayList<Client> clients;
    private ArrayList<Integer> bandwidths;

    /**
     * Basic Constructor
     *
     * @param info: data parsed from input file
     */
    public Solution(Info info) {
        this.info = info;
        this.graph = info.graph;
        this.clients = info.clients;
        this.bandwidths = info.bandwidths;
    }

    /**
     * Method that returns the calculated 
     * SolutionObject as found by your algorithm
     *
     * @return SolutionObject containing the paths, priorities and bandwidths
     */
    public SolutionObject outputPaths() {
        SolutionObject sol = new SolutionObject();
        /* TODO: Your solution goes here */
        return sol;
    }
    
    private int outputPaths_Helper() {
        clients.sort(new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                if (o1.payment > o2.payment) return -1;
                else if (o1.payment < o2.payment) return 1;
                else {
                    if (o1.alpha < o2.alpha) return -1;
                    else if (o1.alpha > o2.alpha) return 1;
                    else return 0;
                }
            }
        });
        return 0;
    }
    
}
