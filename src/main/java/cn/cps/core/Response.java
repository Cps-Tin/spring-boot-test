package cn.cps.core;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

@Data
public class Response<T> {

    private Integer page;

    private Integer size;

    private Integer totalPage;

    private List<T> list;

    public Response() {

    }

    public Response(PageInfo<T> page){
        this.page = page.getPageNum();
        this.size = page.getSize();
        this.totalPage = page.getPages();
        this.list = page.getList();
    }

}
