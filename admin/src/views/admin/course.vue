<template>
  <div>
    <p>
      <button v-on:click="add()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit"></i>
        新增
      </button>
      &nbsp;
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        刷新
      </button>
    </p>

    <pagination ref="pagination" v-bind:list="list"></pagination>

    <div class="row">
      <div v-for="course in courses" class="col-md-3">
        <div class="thumbnail search-thumbnail">
          <img v-show="!course.image" class="media-object" src="/static/image/蝴蝶忍.jpg"/>
          <img v-show="course.image" class="media-object" v-bind:src="course.image"/>
          <div class="caption">
            <div class="clearfix">
              <span class="pull-right label label-primary info-label">
                <td>{{ COURSE_LEVEL | optionKV(course.level) }}</td>
              </span>
              <span class="pull-right label label-primary info-label">
                <td>{{ COURSE_CHARGE | optionKV(course.charge) }}</td>
              </span>
              <span class="pull-right label label-primary info-label">
                <td>{{ COURSE_STATUS | optionKV(course.status) }}</td>
              </span>
            </div>
            <h3 class="search-title">
              <a href="#" class="blue">{{ course.name }}</a>
            </h3>
            <div v-for="teacher in teachers.filter(t=>{return t.id===course.teacherId})"
                 class="profile-activity clearfix">
              <div>
                <img v-show="!teacher.image" class="pull-left" src="/ace/assets/images/avatars/avatar5.png">
                <img v-show="teacher.image" class="pull-left" v-bind:src="teacher.image">
                <a class="user" href="#"> {{ teacher.name }} </a>
                <br>
                {{ teacher.position }}
              </div>
            </div>
            <br>
            <p>{{ course.summary }}</p>
            <p>
              <span class="badge badge-info">{{ course.id }}</span>
              &nbsp;
              <span class="badge badge-info">排序：{{ course.sort }}</span>
              &nbsp;
              <span class="badge badge-info">{{ course.time | formatSecond }}</span>
            </p>
            <br>
            <p>
              <button v-on:click="toChapter(course)" class="btn btn-white btn-xs btn-info btn-round">
                <i class="ace-icon fa fa-book bigger-120"> 大章</i>
              </button>
              &nbsp;
              <button v-on:click="toContent(course)" class="btn btn-white btn-xs btn-info btn-round">
                <i class="ace-icon fa  fa-bookmark-o bigger-120"> 内容</i>
              </button>
              &nbsp;
              <button v-on:click="openSortModal(course)" class="btn btn-white btn-xs btn-info btn-round">
                <i class="ace-icon fa fa-sort bigger-120"> 排序</i>
              </button>
              &nbsp;
              <button v-on:click="edit(course)" class="btn btn-white btn-xs btn-info btn-round">
                <i class="ace-icon fa fa-pencil bigger-120"> 编辑</i>
              </button>
              &nbsp;
              <button v-on:click="del(course.id)" class="btn btn-white btn-xs btn-warning btn-round">
                <i class="ace-icon fa fa-trash-o bigger-120"> 删除</i>
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>

    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label for="name" class="col-sm-2 control-label">分类</label>
                <div class="col-sm-10">
                  <ul id="tree" class="ztree"></ul>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">封面</label>
                <div class="col-sm-10">
                  <big-file
                      v-bind:suffixs="['jpg', 'jpeg', 'png']"
                      v-bind:use="FILE_USE.COURSE.key"
                      v-bind:input-id="'image-upload'"
                      v-bind:text="'上传封面'"
                      v-bind:after-upload="afterUpload"></big-file>
                  <div v-show="course.image" class="row">
                    <div class="col-md-6">
                      <!-- img-responsive 图片自适应 -->
                      <img v-bind:src="course.image" class="img-responsive">
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label for="name" class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                  <input v-model="course.name" type="text" class="form-control" id="name" placeholder="名称">
                </div>
              </div>
              <div class="form-group">
                <label for="teacher" class="col-sm-2 control-label">讲师</label>
                <div class="col-sm-10">
                  <select v-model="course.teacherId" class="form-control" id="teacher">
                    <option v-for="o in teachers" v-bind:value="o.id">{{ o.name }}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label for="summary" class="col-sm-2 control-label">概述</label>
                <div class="col-sm-10">
                  <input v-model="course.summary" type="text" class="form-control" id="summary" placeholder="概述">
                </div>
              </div>
              <div class="form-group">
                <label for="time" class="col-sm-2 control-label">时长</label>
                <div class="col-sm-10">
                  <input v-model="course.time" type="text" class="form-control" id="time" placeholder="时长">
                </div>
              </div>
              <div class="form-group">
                <label for="price" class="col-sm-2 control-label">价格（元）</label>
                <div class="col-sm-10">
                  <input v-model="course.price" type="text" class="form-control" id="price" placeholder="价格（元）">
                </div>
              </div>
              <div class="form-group">
                <label for="level" class="col-sm-2 control-label">级别</label>
                <div class="col-sm-10">
                  <select v-model="course.level" class="form-control" id="level">
                    <option v-for="o in COURSE_LEVEL" v-bind:value="o.key">{{ o.value }}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label for="charge" class="col-sm-2 control-label">收费</label>
                <div class="col-sm-10">
                  <select v-model="course.charge" class="form-control" id="charge">
                    <option v-for="o in COURSE_CHARGE" v-bind:value="o.key">{{ o.value }}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label for="status" class="col-sm-2 control-label">状态</label>
                <div class="col-sm-10">
                  <select v-model="course.status" class="form-control" id="status">
                    <option v-for="o in COURSE_STATUS" v-bind:value="o.key">{{ o.value }}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label for="enroll" class="col-sm-2 control-label">报名数</label>
                <div class="col-sm-10">
                  <input v-model="course.enroll" type="text" class="form-control" id="enroll" placeholder="报名数">
                </div>
              </div>
              <div class="form-group">
                <label for="sort" class="col-sm-2 control-label">顺序</label>
                <div class="col-sm-10">
                  <input v-model="course.sort" type="text" class="form-control" id="sort" placeholder="顺序" disabled>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
              <i class="ace-icon fa fa-times"></i>
              取消
            </button>
            <button type="button" class="btn btn-white btn-info btn-round" v-on:click="save()">
              <i class="ace-icon fa fa-plus blue"></i>
              保存
            </button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    <div id="course-sort-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">排序</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label for="summary" class="col-sm-2 control-label">当前排序</label>
                <div class="col-sm-10">
                  <input class="form-control" v-model="sort.oldSort" name="oldSort" disabled>
                </div>
              </div>
              <div class="form-group">
                <label for="time" class="col-sm-2 control-label">新排序</label>
                <div class="col-sm-10">
                  <input class="form-control" v-model="sort.newSort" name="newSort">
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
              <i class="ace-icon fa fa-times"></i>
              取消
            </button>
            <button type="button" class="btn btn-white btn-info btn-round" v-on:click="updateSort()">
              <i class="ace-icon fa fa-plus blue"></i>
              保存
            </button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
  </div>

</template>

<script>
import Pagination from "../../components/pagination";
import BigFile from "../../components/big-file";

export default {
  components: {Pagination, BigFile},
  name: "business-course",
  data: function () {
    return {
      course: {}, // 用于绑定form表单的数据
      courses: [],
      COURSE_LEVEL: COURSE_LEVEL,
      COURSE_CHARGE: COURSE_CHARGE,
      COURSE_STATUS: COURSE_STATUS,
      FILE_USE: FILE_USE,
      categorys: [],
      tree: {},
      saveContentLabel: "",
      sort: {
        id: "",
        oldSort: 0,
        newSort: 0
      },
      teachers: [],
    }
  },
  mounted: function () {
    // sidebar激活样式方法一
    // this.$parent.activeSidebar("business-course-sidebar");
    let _this = this;
    _this.$refs.pagination.size = 5;
    _this.allCategory();
    _this.allTeacher();
    _this.list(1);
  },
  methods: {
    /**
     * 点击编辑
     * @param course
     */
    edit(course) {
      let _this = this;
      _this.course = $.extend({}, course); // 列表数据复制一份赋值给表单course，防止修改表单时把列表也修改
      // 查找当前结点勾选了哪些分类
      _this.listCategory(course.id);
      $("#form-modal").modal("show");//hide
    },
    /**
     * 点击新增
     */
    add() {
      let _this = this;
      //.modal里的modal是内置的方法,用于弹出或关闭模态框
      _this.course = {
        sort: _this.$refs.pagination.total + 1
      };
      // 让所有的结点都不选中
      _this.tree.checkAllNodes(false);
      $("#form-modal").modal("show");//hide
    },
    /**
     * 列表查询
     * @param page
     */
    list(page) {
      let _this = this;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/course/list", {
        page: page,
        size: _this.$refs.pagination.size
      }).then((response) => {
        Loading.hide();
        let resp = response.data;
        _this.courses = resp.content.list;
        _this.$refs.pagination.render(page, resp.content.total);
      });
    },
    /**
     * 点击保存
     */
    save() {
      let _this = this;

      // 保存校验
      if (1 != 1
          || !Validator.require(_this.course.name, "名称")
          || !Validator.length(_this.course.name, "名称", 1, 50)
          || !Validator.length(_this.course.summary, "概述", 1, 2000)
          || !Validator.length(_this.course.image, "封面", 1, 100)
      ) {
        return;
      }
      //获得当前选中的数据
      let categorys = _this.tree.getCheckedNodes();
      if (Tool.isEmpty(categorys)) {
        Toast.warning("请选择分类");
        return;
      }
      _this.course.categorys = categorys;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/course/save", _this.course).then((response) => {
        Loading.hide();
        let resp = response.data;
        if (resp.success) {
          $("#form-modal").modal("hide");
          _this.list(1);
          Toast.success("保存成功");
        } else {
          Toast.warning(resp.message);
        }
      });
    },
    /**
     * 点击删除
     * @param id
     */
    del(id) {
      let _this = this;
      Confirm.show("确认删除？", "删除后不可恢复，确认删除!", function () {
        _this.$ajax.delete(process.env.VUE_APP_SERVER + "/business/admin/course/delete/" + id).then((response) => {
          let resp = response.data;
          if (resp.success) {
            _this.list(1);
            Toast.success("删除成功");
          }
        })
      });
    },
    /**
     * 点击 大章
     * @param course
     */
    toChapter(course) {
      let _this = this;
      SessionStorage.set(SESSION_KEY_COURSE, course);
      _this.$router.push("/business/chapter");
    },

    /**
     * 列表查询
     */
    allCategory() {
      let _this = this;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/category/all").then((response) => {
        Loading.hide();
        let resp = response.data;
        _this.categorys = resp.content;
        _this.initTree();
      });
    },
    initTree() {
      let _this = this;
      let setting = {
        check: {
          enable: true
        },
        data: {
          simpleData: {
            idKey: "id",
            pIdKey: "parent",
            rootPId: "00000000",
            enable: true
          }
        }
      };

      let zNodes = _this.categorys;

      _this.tree = $.fn.zTree.init($("#tree"), setting, zNodes);

      // 默认展开所有的节点
      // _this.tree.expandAll(true);
    },
    /**
     * 查找课程下所有分类
     * @param courseId
     */
    listCategory(courseId) {
      let _this = this;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/course/list-category/" + courseId).then((response) => {
        Loading.hide();
        console.log("查找课程下所有分类结果：", response);
        let resp = response.data;
        let categorys = resp.content;

        // 勾选查询到的分类
        _this.tree.checkAllNodes(false);
        for (let i = 0; i < categorys.length; i++) {
          let node = _this.tree.getNodeByParam("id", categorys[i].categoryId);
          _this.tree.checkNode(node, true);
        }
      })
    },
    openSortModal(course) {
      let _this = this;
      _this.sort = {
        id: course.id,
        oldSort: course.sort,
        newSort: course.sort
      };
      $("#course-sort-modal").modal("show");
    },
    /**
     * 排序
     */
    updateSort() {
      let _this = this;
      if (_this.sort.newSort === _this.sort.oldSort) {
        Toast.warning("排序没有变化");
        return;
      }
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/course/sort", _this.sort).then((response) => {
        let resp = response.data;

        if (resp.success) {
          Toast.success("更新排序成功");
          $("#course-sort-modal").modal("hide");
          _this.list(1);
        } else {
          Toast.error("更新排序失败");
        }
      });
    },
    allTeacher() {
      let _this = this;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/teacher/all").then((response) => {
        Loading.hide();
        let resp = response.data;
        _this.teachers = resp.content;
      });
    },
    afterUpload(resp) {
      let _this = this;
      let image = resp.content.path;
      _this.course.image = image;

      console.log("文件上传成功：", image);
    },
    /**
     * 点击【内容】
     */
    toContent(course) {
      let _this = this;
      SessionStorage.set(SESSION_KEY_COURSE, course);
      _this.$router.push("/business/content");
    },
  }
}
</script>

<style scoped>
.caption h3 {
  font-size: 20px;
}

@media (max-width: 1199px) {
  .caption h3 {
    font-size: 16px;
  }
}
</style>