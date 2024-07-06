<template>
  <div id="CompressionSetting">
    <div id="CompressionSelect">
       <div class='setting'>设置</div>
       <div class="setting2"><div class="setting2-title">将图像使用什么压缩算法进行压缩：</div>
      <a-select
    label-in-value
    :default-value="{key:CompressionName}"
    style="width: 120px"
    @change="handleChange"
  >
    <a-select-option value="Huffman">
      Huffman
    </a-select-option>
    <a-select-option value="DCT">
      DCT
    </a-select-option>
  </a-select></div>
      </div>

<a-button type="primary" :loading="loading"  @click="compression" class="compression-button">
      开始压缩
    </a-button>
    <button @click="showModal = true">{{ CompressionName }}压缩算法原理展示</button>
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-container">
        <component :is="currentComponent" />
        <button @click="showModal = false">关闭</button>
      </div>
    </div>
     </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import axios from 'axios'
import HuffmanShow from './HuffmanShow.vue'
import ComingSoon from './ComingSoon.vue'
export default {
  data () {
    return {
      loading: false,
      CompressionName: 'Huffman',
      showModal: false
    }
  },
  watch: {
    showModal (newValue) {
      if (newValue) {
        document.body.classList.add('modal-open')
      } else {
        document.body.classList.remove('modal-open')
      }
    }
  },
  components: {
    HuffmanShow,
    ComingSoon
  },
  computed: {
    currentComponent () {
      if (this.CompressionName === 'Huffman') {
        return 'HuffmanShow'
      } else {
        return 'ComingSoon'
      }
    },
    ...mapState(['fileList'])
  },
  methods: {
    ...mapActions(['updateCompressFileList', 'addnewCompressFileList']),
    handleChange (value) {
      this.CompressionName = value.key
    },
    async compression () {
      // console.log('this.fileList')
      // console.log(this.fileList)

      if (!this.fileList || this.fileList.length === 0) {
        this.$message.error('没有文件可以压缩')
        return
      }
      const filesToCompress = this.fileList.filter(file => file.status === 'done').map(file => file.name)
      if (filesToCompress.length === 0) {
        this.$message.error('没有成功上传的文件可以压缩')
        return
      }
      this.loading = true
      // console.log(this.CompressionName + 'kaishiyasuo ' + filesToCompress)
      // console.log(filesToCompress)
      // 压缩文件
      // 压缩文件后上传到服务器
      try {
        const response = await axios.post('http://localhost:8081/api/compress', {
          compressionAlgorithm: this.CompressionName,
          files: filesToCompress
        })
        this.$message.success('文件压缩成功')
        console.log('返回数据')
        console.log(response.data)
        // 更新 Vuex 中的 compressFileList 数据
        this.addnewCompressFileList(response.data.compressedFiles)
      } catch (error) { this.$message.error('压缩失败') } finally { this.loading = false }
    }
  }
}
</script>

<style scoped>
#CompressionSelect{
  width: 50%;
  border:1px solid #ccc;
  border-radius: 3px;
}
.setting{
  border-radius: 3px 3px 0 0;
  padding:10px;
  background-color: #f0f0f0;
  color:#333;
  font-size:14px;
  font-weight:bold;
  border-bottom:1px solid #ccc;
}
.setting2{
background: #ffffff;
padding:10px;
}
.setting2-title{
  margin-top:10px;
  margin-bottom:10px;
  font-size:14px;
  color:#333;
  font-weight:bold;
}

.compression-button{
  margin-top:10px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
   z-index: 999; /* 确保遮罩层在所有元素之上 */
}

.modal-container {
  background-color: white;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
   z-index: 1000; /* 确保弹窗在遮罩层之上 */
}
</style>
