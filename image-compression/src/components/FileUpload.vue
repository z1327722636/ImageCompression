<template>
  <div>
    <a-upload-dragger
      name="file"
      :multiple="true"
      action='http://localhost:8081/api/upload'
      @change="handleChange"
      :before-upload="beforeUpload"
    >
      <p class="ant-upload-drag-icon">
        <a-icon type="inbox" />
      </p>
      <p class="ant-upload-text">
        单击或者拖拽上传文件
      </p>
      <p class="ant-upload-hint">
        <!-- 支持上传文件格式：.doc, .docx, .xls, .xlsx, .ppt, .pptx, .pdf, .txt, .zip, .rar -->
        <slot></slot>
      </p>
    </a-upload-dragger>

  </div>
</template>

<script>
export default {
  data () {
    return {}
  },
  methods: {
    beforeUpload (file, fileList) {
      return new Promise((resolve, reject) => {
        const isImage = file.type.startsWith('image/')
        if (!isImage) {
          reject(this.$message.error('只能上传图片文件！')) // 停止上传
        } else {
          resolve(file) // 开始上传
        }
      })
    },
    handleChange (info) {
      // this.$store.fileList = info.fileList
      this.$store.commit('setFileList', info.fileList)
      // console.log('111')
      // console.log(this.$store.fileList)
      const status = info.file.status
      if (status !== 'uploading') {
        console.log(info.file, info.fileList)
      }
      if (status === 'done') {
        this.$message.success(`${info.file.name} file uploaded successfully.`)
      } else if (status === 'error') {
        this.$message.error(`${info.file.name} file upload failed.`)
      }
    }

  }

}
</script>

<style>
/* Add any necessary styles here */
</style>
