<template>
  <div>
    <button type="button" v-on:click="selectFile()" class="btn btn-white btn-default btn-round">
      <i class="ace-icon fa fa-upload"></i>
      {{ text }}
    </button>
    <input class="hidden" type="file" ref="file" v-on:change="uploadFile()" v-bind:id="inputId+'-input'">
    <p></p>
  </div>
</template>

<script>
export default {
  name: 'file',
  // props，用于父子组件传递数据，可以理解成组件可配置的属性
  props: {
    text: {
      default: "上传文件"
    },
    inputId: {
      default: "file-upload"
    },
    use: {
      default: ""
    },
    suffixs: {
      default: []
    },
    afterUpload: {
      type: Function,
      default: null
    }
  },
  data: function () {
    return {}
  },
  methods: {
    uploadFile() {
      let _this = this;
      let formData = new window.FormData();

      let file = _this.$refs.file.files[0];

      //判断文件格式
      let suffixs = _this.suffixs;
      let fileName = file.name;
      let suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase();
      let validateSuffix = false;
      for (let i = 0; i < suffixs.length; i++) {
        if (suffixs[i].toLowerCase() == suffix) {
          validateSuffix = true;
          break;
        }
      }
      if (!validateSuffix) {
        Toast.warning("文件格式不正确！只支持上传：" + suffixs.join("，"));
        $("#" + _this.inputId + "-input").val("");
        return;
      }

      // key:"file"必须和后端controller参数名一致
      formData.append("file", file);
      formData.append("use", _this.use);
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/file/admin/oss-simple", formData).then((response) => {
        Loading.hide();
        let resp = response.data;
        _this.afterUpload(resp);
      })
    },
    selectFile() {
      let _this = this;
      $("#" + _this.inputId + "-input").trigger("click");
    }
  }
}
</script>