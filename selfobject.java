import java.util.*;

public class SelfObject {

    // maps slot name -> referenced object
    // collection of slots
    private Map<String, SelfObject> slots = new HashMap<>();

    // slots that are parents
    // used only during message lookup
    private Set<String> parentSlots = new HashSet<>();

    // behavior fields 
    // if not null, this object behaves like a method
    // evaluating will copy and send these messages
    private List<String> messages = null;

    // if not null, this object is a primitive value
    private Object primitiveValue = null;

    // if not null, this object is a primitive function
    private Primitive primitiveFunction = null;

    // cababilities
    // evaluate
    public SelfObject evaluate() {

        // returns a copy
        if (primitiveValue != null) {
            return copy();
        }

        // copy first
        if (primitiveFunction != null) {
            SelfObject copy = copy();
            return primitiveFunction.apply(copy);
        }

        // list of messages that behaves like a function call
        // copy first, send messages, return result of last message
        if (messages != null) {
            SelfObject copy = copy();
            SelfObject result = copy;

            for (String message : messages) {
                result = copy.sendAMessage(message);
            }
            return result;
        }

        // otherwise, just return a copy
        return this;
    }

    // copy
    public SelfObject copy() {

        SelfObject copy = new SelfObject();
        // copy slot references
        copy.slots.putAll(this.slots);
        // copy parent designations
        copy.parentSlots.addAll(this.parentSlots);
        // copy behavior information
        copy.messages = this.messages;
        copy.primitiveValue = this.primitiveValue;
        copy.primitiveFunction = this.primitiveFunction;

        return copy;
    }

    // sendAMessage
    // BFS lookup slot with the same name, evaluate found object, return the resuklt
    public SelfObject sendAMessage(String message) {
        SelfObject target = bfsLookup(message);
        return target.evaluate();
    }

    // sendAMessageWithParameters
    // copy the found object, set the slot to parameters, evaluate the copy
    public SelfObject sendAMessageWithParameters(String message, SelfObject parameter) {

        SelfObject target = bfsLookup(message).copy();

        // store the argument inside the object
        target.assignSlot("parameter", parameter);
        return target.evaluate();
    }

    // bfslookup
    // searching for self, parents, parents of parents
    private SelfObject bfsLookup(String message) {

        Queue<SelfObject> queue = new LinkedList<>();
        Set<SelfObject> visited = new HashSet<>();

        queue.add(this);

        while (!queue.isEmpty()) {
            SelfObject current = queue.poll();

            // check if the current object has the slot
            if (current.slots.containsKey(message)) {
                return current.slots.get(message);
            }

            visted.add(current);

            // add parent slots to the queue
            for (String parentSlot : current.parentSlots) {
                SelfObject parent = current.slots.get(parentSlot);

                if (!visited.contains(parent)) {
                    queue.add(parent);
                }
            }

        throw new RuntimeException("Slot not found: " + message);
    }

    // assignSlot
    // set slot name 
    public void assignSlot(String slotName, SelfObject value) {
        slots.put(slotName, value);
    }

    // makeParent
    // mark an existing slot as a parent slot
    public void makeParent(String slotName) {
        parentSlots.add(slotName);
    }

    // assignParentSlot
    public voic assignParentSlot(String slotName, SelfObject value) {
        assignSlot(slotName, value);
        makeParent(slotName);
    }

    // print
    // representation of the object
    public String print() {

        // primitive prints directly
        if (primitiveValue != null) 
            return primitiveValue.toString();
        
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");

        for (String k : slots.keySet()) {
            sb.append(k);

            if (parentSlots.contains(k))
                sb.append(" (parent)");
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }

    // helper methods for building objects
    public void setMessages(List<String> msgs) {
        messages = msgs;
    }

    public void setPrimitiveValue(Object value) {
        primitiveValue = value;
    }

    public void setPrimitiveFunction(Primitive function) {
        primitiveFunction = function;
    }
        
}

}
