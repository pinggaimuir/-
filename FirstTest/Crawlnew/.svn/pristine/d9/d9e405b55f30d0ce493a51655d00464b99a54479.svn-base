
--meta如果批量建表，逐个添加产业链会非常麻烦，因此通过脚本批量添加
-- a
-- 备份数据
--select * into [backup].dbo.CFT_TABLE_MENU_CYL_NEW_20160814 from [bricdata].dbo.cft_table_menu_cyl_new

declare @iid int,@did int,@vid int,@id int
declare cylCursor Cursor local for 
	select industryId,industryDetailId,varid,id
    from CFG_TABLE_META_NEW meta
    where id>=111217 and id<=111367
    order by industryId ,industryDetailId,varid,id
open cylCursor
fetch next from cylCursor into @iid,@did,@vid,@id
while @@fetch_status = 0
BEGIN
	--detail sort
	declare @dsort int,@dsortE int
	select @dsortE=0
	declare @sql nvarchar(1000)
	select @dsort=max(industry_detail_sort) from CFT_TABLE_MENU_CYL_NEW where industry_id=@iid and industry_detail_id=@did
	if(@dsort is null )
	begin
		select @dsort=max(industry_detail_sort)+1 from CFT_TABLE_MENU_CYL_NEW where industry_id=@iid 
		if(@dsort is null)
		begin
			set @dsort =1
		end
		select @dsortE = 1
	end

	---var  sort
	declare @vsort int,@vsortE int
	set @vsortE=0 
	select @vsort = max(varSort) from CFT_TABLE_MENU_CYL_NEW where industry_id=@iid and industry_detail_id=@did and var_id=@vid
	if(@vsort is null)
	begin		
		if(@dsortE=1)--新的子行业
		begin
			select @vsort = 1
		end
		else
		begin
			select @vsort=max(varSort)+1 from CFT_TABLE_MENU_CYL_NEW where industry_id=@iid and industry_detail_id=@did			
		end
		select @vsortE = 1
	end
	
	declare @tsort int 
	select @tsort=max(tableSort) from CFT_TABLE_MENU_CYL_NEW where industry_id=@iid and industry_detail_id=@did and var_id=@vid and table_meta_id=@id
	if(@tsort is not null)
	begin
		continue
	end
	if(@vsortE=1)
	begin
		select @tsort = 1
	end
	else
	begin	
		select @tsort=max(tableSort)+1 from CFT_TABLE_MENU_CYL_NEW where industry_id=@iid and industry_detail_id=@did and var_id=@vid
	end

	--print ' '+str(@iid)+','+str(@did)+','+str(@dsort)+','+str(@vid)+','+str(@vsort)+','+str(@tsort)+','+str(@id)
	insert into CFT_TABLE_MENU_CYL_NEW values(@iid,@did,@dsort,@vid,@vsort,@tsort,@id)
	fetch next from cylCursor into @iid,@did,@vid,@id
end
close cylCursor
deallocate cylCursor

--select * into [bricdata].dbo.CFT_TABLE_MENU_CYL from [backup].dbo.cft_table_menu_cyl_20165019








