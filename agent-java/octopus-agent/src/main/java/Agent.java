import java.util.PriorityQueue;
import java.util.Queue;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 16-07-13
 * Time: 14:20
 */
public class Agent
{
    private Queue<Task> _taskQueue;
    private AgentRepository _agentRepository;

    public Agent(AgentRepository repository)
    {
        if(repository == null) throw new NullPointerException("repository can't be null");

        set_agentRepository(repository);
        set_taskQueue(new PriorityQueue<Task>());

        sync();
    }

    private void sync()
    {
        for (Task task : _agentRepository.getAvailableTasks())
        {
            _taskQueue.add(task);
        }
    }

    private AgentRepository get_agentRepository()
    {
        return _agentRepository;
    }

    private void set_agentRepository(AgentRepository agentRepository)
    {
        _agentRepository = agentRepository;
    }

    public Queue<Task> get_taskQueue()
    {
        return _taskQueue;
    }

    public void set_taskQueue(Queue<Task> taskQueue)
    {
        _taskQueue = taskQueue;
    }
}
