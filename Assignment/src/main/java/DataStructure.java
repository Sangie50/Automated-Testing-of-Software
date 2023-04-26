import java.util.*;
public class DataStructure {
    private String[] conditions;
    private String branchPredicate;
    public DataStructure[] dsArray;
    
    public DataStructure(String[] cond, String branchP){
        this.conditions = cond;
        this.branchPredicate = branchP;
    }

    public DataStructure(){
        init();
    }

    public String[] getConditions(){
        return conditions;
    }

    public String getBranchPredicate(){
        return branchPredicate;
    }

    public DataStructure[] getDS() {
        return dsArray;
    }

    public final void init(){
        String[] allConditions = {"side1 > side2", "side1 > side3", "side2 > side3", "side1 + side2 <= side3", "side1 == side2", "side2 == side3", "side2 == side3",
                                    "month1 < 1", "month2 < 1", "month1 > 12", "month2 > 12", "day1 < 1", "day2 < 1", "day1 > daysInMonth(month1, year1)", 
                                    "day2 > daysInMonth(month2, year2)", "year2 < year1", "year2 == year1", "month2 < month1", "year2 == year1", 
                                    "month2 == month1", "day2 < day1", "month1 == month2", "year1 == year2", "year1 == year2", "month < month2", 
                                    "month <= 12", "month < month2", "year < year2", "bmi < 18.5", "bmi >= 17.5", "bmi < 25", "bmi >= 25", "bmi < 30"};
                                    
        String adj2 = String.format("%s && %s", allConditions[16], allConditions[17]);
        String adj3 = String.format("%s && %s && %s", allConditions[18], allConditions[19], allConditions[20]);
        String adj4 = String.format("%s && %s", allConditions[21], allConditions[22]);
        String adj5 = String.format("%s && %s", allConditions[29], allConditions[30]);
        String adj6 = String.format("%s && %s", allConditions[31], allConditions[32]);
        
        String[] allAdjuncts = {adj2, adj3, adj4, adj5, adj6};
        
        String bp1 = String.format("(%s) || (%s) || (%s)", allConditions[15], adj2, adj3);
        String[] overallBranchPredicate = {allConditions[0], allConditions[1], allConditions[2], allConditions[3], allConditions[4], allConditions[5], allConditions[6], 
                                            allConditions[7], allConditions[8], allConditions[9], allConditions[10], allConditions[11], allConditions[12], 
                                            allConditions[13], allConditions[14], bp1, adj4, allConditions[23], allConditions[24], allConditions[25], 
                                            allConditions[26],  allConditions[27], adj5, adj6};

        dsArray = new DataStructure[overallBranchPredicate.length];    
                   
        //dataStructure with all the branch predicates and its conditions
        for (int i = 0; i < overallBranchPredicate.length; i++){
            if ( i < 17){
                if (i != 15 && i != 16) {
                    String[] c = {allConditions[i]};
                    DataStructure ds = new DataStructure(c, overallBranchPredicate[i]);
                    dsArray[i] = ds;
                }
                else if (i == 15){
                    String[] c = {allConditions[15], allConditions[16], allConditions[17], allConditions[18], allConditions[19], allConditions[20]};
                    DataStructure ds = new DataStructure(c, overallBranchPredicate[15]);
                    dsArray[i] = ds;
                }
                else if (i == 16){
                    String[] c = {allConditions[21], allConditions[22]};
                    DataStructure ds = new DataStructure(c, overallBranchPredicate[16]);
                    dsArray[i] = ds;
                }

            }
            if( i >= 17 && i < overallBranchPredicate.length){
                if (i != 22 && i != 23) {
                    String[] c = {allConditions[i+6]};
                    DataStructure ds = new DataStructure(c, overallBranchPredicate[i]);
                    dsArray[i] = ds;
                }
                else if (i == 22){
                    String[] c = {allConditions[29], allConditions[30]};
                    DataStructure ds = new DataStructure(c, overallBranchPredicate[i]);
                    dsArray[i] = ds;
                }
                else if (i == 23){
                    String[] c = {allConditions[31], allConditions[32]};
                    DataStructure ds = new DataStructure(c, overallBranchPredicate[i]);
                    dsArray[i] = ds;
                }
            }
        }          
    }
}

