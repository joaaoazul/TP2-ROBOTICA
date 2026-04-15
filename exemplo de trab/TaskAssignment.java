/**
 * Couples a task with its pre-computed execution plan.
 */
public record TaskAssignment(Task task, TaskPlan plan) {
}
