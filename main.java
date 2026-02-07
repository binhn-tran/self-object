import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. evaluate
        // primitive value returns a copy with the same value when evaluated
        SelfObject primitiveFive = new SelfObject();
        primitiveFive.setPrimitiveValue(5);

        SelObject evaluatedResult = primitiveFive.evaluate();

        System.out.println("Expected: 5");
        System.out.println("Actual: " + evaluatedResult.print());

        // 2. copy
        // copy() duplicate slots and behavior
        SelfObject original = new SelfObject();
        original.assignSlot("num", primitiveFive);

        SelfObject copy = original.copy();
        SelfObject copyResult = copy.sendAMessage("num");
        System.out.println("Copied slot value: " + copyResult.print());

        // 3. sendAMessage
        // lookup slot and evaluate it
        SelfObject sendMessageResult = original.sendAMessage("num");
        System.out.println("Expected: 5");
        System.out.println("Actual: " + sendMessageResult.print());

        // 4. sendAMessageWithParameters
        // pass the object through the parameter slot
        SelfObject parameterObject = new SelfObject();
        parameterObject.setMessages(List.of("parameter"));

        SelfObject primitiveTen = new SelfObject();
        primitiveTen.setPrimitiveValue(10);

        SelfObject parameterResult = parameterObject.sendAMessageWithParameters("parameter", primitiveTen);

        System.out.println("Expected: 10");
        System.out.println("Actual: " + parameterResult.print());

        // 5. assignSlot
        // adds a new slot to the object
        SelfObject slotObject = new SelfObject();
        slotObject.assignSlot("value", primitiveFive);

        System.out.println("Slot assigned. Lookup result: "
                + slotTestObject.sendAMessage("value").print());
        
        // 6. makeParent
        // mark an existing slot as a parent and lookup the parent slot
        SelfObject parentSlot = new SelfObject();
        parentSlot.assignSlot("parentValue", primitiveTen);
        SelfObject childSlot = new SelfObject();
        childSlot.assignSlot("parent", parentSlot);
        // mark as the parent slot
        childSlot.makeParent("parent");

        System.out.println("Inherited value: "
                + childObject.sendAMessage("shared").print());

        // 7. assignParentSlot 
        // combination of assignSlot and makeParent
        SelfObject anotherChild = new SelfObject();
        anotherChild.assignParentSlot("parent", parentSlot);

        System.out.println("Inherited value: "
                + anotherChild.sendAMessage("shared").print());

        // 8. print
        // print representation of the object
        System.out.println("Parent object: " + parentSlot.print());
        System.out.println("Child object:  " + childObject.print());


        
    }
}

