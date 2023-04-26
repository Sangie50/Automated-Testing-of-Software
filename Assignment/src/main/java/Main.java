import java.util.*;

import javax.script.ScriptException;
public class Main {
    public static void main(String[] args) throws ScriptException {
        DataStructure ds = new DataStructure();
        DataStructure[] originalDataStructure = ds.getDS();
        DataStructure[] updatedDataStructure = new DataStructure[originalDataStructure.length];

        //check if there are similar conditions in the data structure
        //if so, then update conditions and the branch predicate
        for(int i = 0; i < originalDataStructure.length; i++){
            String[] originalConds = originalDataStructure[i].getConditions();
            String originalBP = originalDataStructure[i].getBranchPredicate();
            List<String> updatedConds = Conditions.similarConds(originalConds);

            String[] updatedCondsArray = updatedConds.toArray(new String[0]);
            String updatedBP = Conditions.updateBranchPredicate(originalConds, updatedConds, originalBP);
            DataStructure updatedDs = new DataStructure(updatedCondsArray, updatedBP);

            updatedDataStructure[i] = updatedDs;
        }

        //example
        //get test requirements for a specific data structure from an array of updateDataStructure
        DataStructure exampleDS = updatedDataStructure[16];
        List<String> exampleCondsList = Arrays.asList(exampleDS.getConditions());
        String exampleBP = exampleDS.getBranchPredicate();

        HashMap<String, Set<HashMap<List<Boolean>, Boolean>>> restrictedMCDC = TestRequirements.restrictedMCDC(exampleBP, exampleCondsList);
        HashMap<String, HashMap<List<Boolean>, Boolean>> correlatedMCDC = TestRequirements.correlatedMCDC(exampleBP, exampleCondsList);
    }
    
}
