function deleteFruit(fid) {
    if(confirm('是否确认删除？')){
        alert(fid);
        window.location.href='delete.do?fid='+fid;
    }
}