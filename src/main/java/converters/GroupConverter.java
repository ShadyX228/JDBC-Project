package converters;

import dbmodules.entity.Group;
import dbmodules.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class GroupConverter implements Converter<String, Group> {
    private GroupService groupService;

    @Autowired
    public GroupConverter(GroupService groupService){
        this.groupService = groupService;
    }

    @Override
    public Group convert(String groupId) {
        int id = Integer.parseInt(groupId);
        return groupService.selectById(id);
    }
}