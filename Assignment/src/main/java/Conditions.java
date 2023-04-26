import java.util.*;
public class Conditions {

    //check if there are similar conditions
    public static List<String> similarConds(String[] conds){
        List<String> condsList = new ArrayList<>(Arrays.asList(conds));
        for (int i = 0; i < conds.length; i++){
            String currentCond = conds[i];

            String reverseCond = new StringBuilder(currentCond).reverse().toString();            
            condsList.removeIf(j -> Objects.equals(j, reverseCond));

            if (currentCond.contains(">")){
                condsList.removeIf(j -> Objects.equals(j, currentCond.replaceAll(">", "<=")));
            }
            else if (currentCond.contains("<=")){
                condsList.removeIf(j -> Objects.equals(j, currentCond.replaceAll("<=", ">")));
            }

            if (currentCond.contains("==")){
                condsList.removeIf(j -> Objects.equals(j, currentCond.replaceAll("==", "!=")));
            }
            else if (currentCond.contains("!=")){
                condsList.removeIf(j -> Objects.equals(j, currentCond.replaceAll("!=", "==")));
            }
                
        }
        return condsList;
    }

    public static String updateBranchPredicate(String[] previousConds, List<String> updatedConds, String branchPredicate){
        List<String> previousCondsList = new ArrayList<>(Arrays.asList(previousConds));
        previousCondsList.removeAll(updatedConds); //gives the list of deleted conditions
        
        for (int i = 0; i < previousCondsList.size(); i++ ){
            int lastWord = branchPredicate.indexOf(previousCondsList.get(i)) + (previousCondsList.get(i)).length();
            if (lastWord == branchPredicate.length()){
                branchPredicate = (branchPredicate.replace(previousCondsList.get(i), ""));
                branchPredicate = branchPredicate.substring(0, branchPredicate.length()-3);
            }
            else {
                int startIndex = branchPredicate.indexOf(" " + previousCondsList.get(i));
                int endIndex = startIndex + (previousCondsList.get(i)).length() + 4;
                String condWithOperator = branchPredicate.substring(startIndex, endIndex);
                branchPredicate = (branchPredicate.replace(condWithOperator, ""));
            }
        }
        return branchPredicate;
    }
}
