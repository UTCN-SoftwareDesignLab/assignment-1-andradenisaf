package data_access_layer;

import model.ActivityLog;

import java.util.List;

public interface IActivityLogDAO {

    public boolean save(ActivityLog activityLog);

    public boolean delete(int id);

    public boolean update(ActivityLog activityLog);

    public ActivityLog getActivityLogById(int id);

    public List<ActivityLog> getActivityLogs();
}
