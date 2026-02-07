import java.util.*;

// SelfObject class implements Self 
public class SelfObject {

    // Maps slot names strings to other SelfObjects
    // This represents the object's slots its fields or members
    private Map<String, SelfObject> slots = new HashMap<>();

    // A set of slot names that are designated as parent slots
    // Used for inheritance and message lookup
    private Set<String> parentSlots = new HashSet<>();

    // List of messages (strings) that define the object's behavior
    // If not null, evaluating the object will send these messages to a copy
    private List<String> messages = null;

    // Primitive value stored in the object (e.g., a number)
    // If not null, evaluating the object produces a copy with this value
    private Object primitiveValue = null;

    // Primitive function stored in the object
    // If not null, evaluating the object applies this function to a copy
    private Primitive primitiveFunction = null;


    // Capabilities

    /**
     * Evaluate the object
     * If it has a primitive value, return a copy of itself
     * If it has a primitive function, apply it to a copy
     * If it has a list of messages (like a function body), copy itself, send messages, return last result
     * Otherwise, return itself
     */
    public SelfObject evaluate() {
        if (primitiveValue != null) {
            return copy();
        }
        if (primitiveFunction != null) {
            SelfObject copy = copy();
            return primitiveFunction.apply(copy);
        }
        if (messages != null) {
            SelfObject copy = copy();
            SelfObject result = copy;
            for (String message : messages) {
                result = copy.sendAMessage(message);
            }
            return result;
        }
        return this;
    }

    /**
     * Create a copy of this object
     * Copies slots and parent slots
     * Copies messages, primitive value, and primitive function
     */
    public SelfObject copy() {
        SelfObject copy = new SelfObject();
        copy.slots.putAll(this.slots);
        copy.parentSlots.addAll(this.parentSlots);
        copy.messages = this.messages;
        copy.primitiveValue = this.primitiveValue;
        copy.primitiveFunction = this.primitiveFunction;
        return copy;
    }

    /**
     * Send a message to this object
     * Performs a BFS search for a slot with the message name
     * Evaluates the found object and returns the result
     */
    public SelfObject sendAMessage(String message) {
        SelfObject target = bfsLookup(message);
        return target.evaluate();
    }

    /**
     * Send a message with parameters
     * Finds the slot by name, copies it, assigns the parameter to the "parameter" slot
     * Evaluates the copy and returns the result
     */
    public SelfObject sendAMessageWithParameters(String message, SelfObject parameter) {
        SelfObject target = bfsLookup(message).copy();
        target.assignSlot("parameter", parameter);
        return target.evaluate();
    }

    /**
     * Breadth-first search to find a slot
     * Searches this object first, then its parent slots recursively
     * Throws an exception if slot not found
     */
    private SelfObject bfsLookup(String message) {
        Queue<SelfObject> queue = new LinkedList<>();
        Set<SelfObject> visited = new HashSet<>();

        queue.add(this);

        while (!queue.isEmpty()) {
            SelfObject current = queue.poll();

            // Check current object's slots
            if (current.slots.containsKey(message)) {
                return current.slots.get(message);
            }

            visited.add(current);

            // Add parent slots to the queue
            for (String parentSlot : current.parentSlots) {
                SelfObject parent = current.slots.get(parentSlot);
                if (parent != null && !visited.contains(parent)) {
                    queue.add(parent);
                }
            }
        }

        throw new RuntimeException("Slot not found: " + message);
    }

    /**
     * Assign a slot to the object
     * @param slotName Name of the slot
     * @param value SelfObject to assign
     */
    public void assignSlot(String slotName, SelfObject value) {
        slots.put(slotName, value);
    }

    /**
     * Designate a slot as a parent slot
     * @param slotName Name of the slot to mark as parent
     */
    public void makeParent(String slotName) {
        parentSlots.add(slotName);
    }

    /**
     * Assign a slot and make it a parent
     * @param slotName Name of the slot
     * @param value SelfObject to assign
     */
    public void assignParentSlot(String slotName, SelfObject value) {
        assignSlot(slotName, value);
        makeParent(slotName);
    }

    /**
     * Print a textual representation of the object
     * - Shows slots and marks parent slots
     * - Prints primitive value if present
     */
    public String print() {
        if (primitiveValue != null) {
            return primitiveValue.toString();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for (String k : slots.keySet()) {
            sb.append(k);
            if (parentSlots.contains(k)) sb.append(" (parent)");
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }

    // Helper setters

    public void setMessages(List<String> msgs) {
        messages = msgs;
    }

    public void setPrimitiveValue(Object value) {
        primitiveValue = value;
    }

    public void setPrimitiveFunction(Primitive function) {
        primitiveFunction = function;
    }

    // Primitive interface

    // Interface for primitive functions
    // Allows defining arithmetic or other basic operations
    public interface Primitive {
        SelfObject apply(SelfObject obj);
    }

}
