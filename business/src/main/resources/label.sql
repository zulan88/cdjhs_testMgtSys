delimiter $$
drop function if exists get_parent_list$$
create function get_parent_list(in_id varchar(10)) returns varchar(1000)
begin
    declare ids varchar(1000);
    declare tempid varchar(10);

    set tempid = in_id;
    while tempid is not null do
            set ids = CONCAT_WS(',',ids,tempid);
            select parent_Id into tempid from tj_labels where id=tempid;
    end while;
    return ids;
end
$$
delimiter ;