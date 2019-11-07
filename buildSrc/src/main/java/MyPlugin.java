import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.stream.IntStream;

/**
 * java类 gradle plugin
 * 用于创建 10 个 task
 *
 * Created by lsd
 * 2019-11-07 17:32
 */
public class MyPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        IntStream.range(0, 10).forEach(i -> target.task("task" + i));
    }

}
