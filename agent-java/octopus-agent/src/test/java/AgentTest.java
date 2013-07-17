import org.junit.Test;

import java.util.ArrayList;
import java.util.Queue;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 5/1/13
 * Time: 2:03 AM
 */

public class AgentTest {

    AgentRepository _repo = createMock(AgentRepository.class);

    @Test
    public void constructor_default_populateTheQueueWithAvailableJobs()
    {
        Task task             = Make_Task();
        ArrayList<Task> tasks = new ArrayList<Task>();
        tasks.add(task);
        expect(_repo.getAvailableTasks()).andReturn(tasks);
        replayAll();

        Agent agent = Make_Agent();

        Queue<Task> result = agent.get_taskQueue();
        assertEquals(result.size(), 1);
        assertEquals(result.element(), task);
    }

    private Task Make_Task()
    {
        return new Task();
    }

    private Agent Make_Agent()
    {
        return new Agent(_repo);
    }
}
