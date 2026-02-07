import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Evaluate
        // Primitive value: evaluating returns a copy of itself
        SelfObject primitiveFive = new SelfObject();
        primitiveFive.setPrimitiveValue(5);

        SelfObject evaluatedResult = primitiveFive.evaluate();

        System.out.println("Expected: 5");
        System.out.println("Actual: " + evaluatedResult.print());


        // 2. Copy
        // Copy duplicates slots, parents, and behavior
        SelfObject original = new SelfObject();
        original.assignSlot("num", primitiveFive);

        SelfObject copy = original.copy();
        SelfObject copyResult = copy.sendAMessage("num"); // should access "num" in copy

        System.out.println("Copied slot value: " + copyResult.print());

        // 3. Send a message
        // Lookup slot in object or parent slots (BFS) and evaluate
        SelfObject sendMessageResult = original.sendAMessage("num");

        System.out.println("Expected: 5");
        System.out.println("Actual: " + sendMessageResult.print());


        // 4. Send a message with parameters
        // Function-like behavior: pass a parameter object
        SelfObject parameterObject = new SelfObject();
        parameterObject.setMessages(List.of("parameter")); // this object expects a "parameter" message

        SelfObject primitiveTen = new SelfObject();
        primitiveTen.setPrimitiveValue(10);

        SelfObject parameterResult =
                parameterObject.sendAMessageWithParameters("parameter", primitiveTen);

        System.out.println("Expected: 10");
        System.out.println("Actual: " + parameterResult.print());


        // 5. Assign slot
        // Add a new slot to an object
        SelfObject slotObject = new SelfObject();
        slotObject.assignSlot("value", primitiveFive);

        System.out.println("Slot assigned. Lookup result: "
                + slotObject.sendAMessage("value").print());


        // 6. Make Parent Slot
        // Mark an existing slot as a parent
        // Child can inherit slots from parent from a  BFS lookup
        SelfObject parentSlot = new SelfObject();
        parentSlot.assignSlot("shared", primitiveTen);  // parent has a "shared" value

        SelfObject childSlot = new SelfObject();
        childSlot.assignSlot("parent", parentSlot);     // attach parent object
        childSlot.makeParent("parent");                 // mark "parent" as a parent slot

        System.out.println("Inherited value: "
                + childSlot.sendAMessage("shared").print());


        // 7. Assign parent slot
        // Combination of assignSlot + makeParent
        SelfObject anotherChild = new SelfObject();
        anotherChild.assignParentSlot("parent", parentSlot);

        System.out.println("Inherited value: "
                + anotherChild.sendAMessage("shared").print());


        // 8. Print
        // Show textual representation of objects
        System.out.println("Parent object: " + parentSlot.print());
        System.out.println("Child object:  " + childSlot.print());
    }
}

