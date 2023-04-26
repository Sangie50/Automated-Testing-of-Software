import java.util.*;
import javax.script.*;

public class TestRequirements{

    //HashMap<"Branch predicate name/key", HashMap<BP Boolean, List<Conditions Boolean>>>
    public static HashMap<String, Set<HashMap<List<Boolean>, Boolean>>> restrictedMCDC(String updatedBP, List<String> uniqueCond) throws ScriptException{
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript"); 
        HashMap<String, Set<HashMap<List<Boolean>, Boolean>>> restrictedTestReqMap = new HashMap<>();
        HashMap<List<Boolean>, Boolean> returnCondList = new HashMap<>();
        Set<HashMap<List<Boolean>, Boolean>> returnWholeSet = new HashSet<>();

        final List<Boolean> defaultStrBoolCondList = new ArrayList<>(Collections.nCopies(uniqueCond.size(), Boolean.FALSE));
        String defaultBranchPredicate = updatedBP;
        //setting the branch predicate to false by setting all conditions to false - default
        for (int i = 0; i < uniqueCond.size(); i++){
            defaultBranchPredicate = defaultBranchPredicate.replace(uniqueCond.get(i), defaultStrBoolCondList.get(i).toString());
        }
        final String defaultStrBoolBPFinal = defaultBranchPredicate.toLowerCase();
        
        boolean defaultBooleanBP = (Boolean) engine.eval(defaultStrBoolBPFinal);
        returnCondList.put(defaultStrBoolCondList, defaultBooleanBP);
        returnWholeSet.add(returnCondList);

        //setting each condition to true (others are dafault/false) and checking if BP flips to true
        for (int i = 0; i < uniqueCond.size(); i++){
            List<Boolean> changedConds =  new ArrayList<>(Collections.nCopies(uniqueCond.size(), Boolean.FALSE));
            String bp = updatedBP;
            if (updatedBP.contains(uniqueCond.get(i) + " &&")){
                bp = bp.replace(uniqueCond.get(i+1), Boolean.TRUE.toString());
                changedConds.set(i+1, Boolean.TRUE);
            }
            if (updatedBP.contains("&& " + uniqueCond.get(i))){
                bp = bp.replace(uniqueCond.get(i-1), Boolean.TRUE.toString());
                changedConds.set(i-1, Boolean.TRUE);
            }
            bp = bp.replace(uniqueCond.get(i), Boolean.TRUE.toString());
            for (int j = 0; j < uniqueCond.size(); j++){
                if (j != i){
                    bp = bp.replace(uniqueCond.get(j), Boolean.FALSE.toString());
                }
            }
            boolean booleanBP = (Boolean) engine.eval(bp);
            if (booleanBP){
                changedConds.set(i, Boolean.TRUE);

                HashMap<List<Boolean>, Boolean> a = new HashMap<List<Boolean>, Boolean>();
                a.put(changedConds, booleanBP);
                returnWholeSet.add(a);
            }
        }
        restrictedTestReqMap.put(updatedBP, returnWholeSet);
        return restrictedTestReqMap;
    }

    public static HashMap<String, HashMap<List<Boolean>, Boolean>> correlatedMCDC(String updatedBP, List<String> uniqueCond) throws ScriptException{
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript"); 
        HashMap<String, HashMap<List<Boolean>, Boolean>> correlatedTestReqMap = new HashMap<>();

        //computing all the possible combinations of boolean values for the given conditions
        int noOfPossibleCombo = (int) Math.pow(2, uniqueCond.size());
        List<List<Boolean>> truthTable = new ArrayList<>(noOfPossibleCombo);
        for (int i = 0; i < noOfPossibleCombo; i++) {
            List<Boolean> eachCombo = new ArrayList<>(uniqueCond.size());
            for (int j = uniqueCond.size()-1; j>=0; j--) {
                if((i/(int) Math.pow(2, j))%2 == 1){
                    eachCombo.add(true);
                }
                else {
                    eachCombo.add(false);
                }
            }
            truthTable.add(eachCombo);
        }

        //computing branch predicate values for all the possibilities and storing them as hashmap
        //HashMap< List<Possible condition values>, value of branch predicate >
        HashMap<List<Boolean>, Boolean> bpTruthTable = new HashMap<>(); 
        for (int i = 0; i < truthTable.size(); i++) {
            Boolean bpBool;
            String bp = updatedBP;
            for (int j = 0; j < truthTable.get(i).size(); j++) {
                bp = bp.replace(uniqueCond.get(j), (truthTable.get(i)).get(j).toString());
            }
            bpBool = (Boolean) engine.eval(bp);
            bpTruthTable.put(truthTable.get(i), bpBool);
        }

        //finding out which 2 possibilities from truth table flips bp value for a given major condition
        //major condition = int j (for loop)
        HashMap<List<Boolean>, Boolean> correlatedCases = new HashMap<>(); 
        for (int j = 0; j < truthTable.get(0).size(); j++) {
            Random rand = new Random();
            int firstBpIndex = rand.nextInt(bpTruthTable.size());
            int k = 0;
            Boolean findAgain = true;
            boolean condValue1 = (truthTable.get(firstBpIndex)).get(j); //n - random branch predicate, j - major condition
            Boolean bpValue1 = bpTruthTable.get(truthTable.get(firstBpIndex));
            
            while (k < 5 && findAgain){
                int anotherBpIndex = rand.nextInt(bpTruthTable.size());
                boolean condValue2 = (truthTable.get(anotherBpIndex)).get(j); //n - random branch predicate, j - major condition
                Boolean bpValue2 = bpTruthTable.get(truthTable.get(anotherBpIndex));
                
                if ((bpValue1 != bpValue2) && (condValue1 != condValue2)){
                    //store both index 
                    correlatedCases.put(truthTable.get(firstBpIndex), bpTruthTable.get(truthTable.get(firstBpIndex)));
                    correlatedCases.put(truthTable.get(anotherBpIndex), bpTruthTable.get(truthTable.get(anotherBpIndex)));
                    findAgain = false;
                }
                else{
                    //do again
                    k++;
                }
            }
        }
        correlatedTestReqMap.put(updatedBP, correlatedCases);
        return correlatedTestReqMap;
    }
}