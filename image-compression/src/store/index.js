// store/index.js
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  strict: true,
  state: {
    // 在这里定义您的应用程序级别状态
    fileList: [],
    compressFileList: [
      {
        originalName:
          '例子.bmp',
        compressedName: '例子-compressed.hfmc',
        algorithm: 'Huffman',
        originalSize: '原始文件数据',
        compressedSize: '压缩后数据',
        compressionRate: '10%',
        thumbnail:
          'https://img2.baidu.com/it/u=1511254338,3060379644&fm=253&fmt=auto&app=138&f=JPEG?w=670&h=447',
        checked: false
      }
    ]
  },
  mutations: {
    // 在这里定义用于修改状态的 mutation
    setFileList (state, fileList) {
      state.fileList = fileList
    },
    setCompressFileList (state, compressFileList) {
      // state.compressFileList = state.compressFileList.concat(compressFileList)
      state.compressFileList = compressFileList
    },
    addCompressFileList (state, compressFileList) {
      state.compressFileList = state.compressFileList.concat(compressFileList)
    },
    removeCompressFileList (state, index) {
      state.compressFileList.splice(index, 1)
    }
  },
  actions: {
    // 在这里定义用于触发 mutation 的 action
    updateCompressFileList ({ commit }, compressFileList) {
      commit('setCompressFileList', compressFileList)
    },
    addnewCompressFileList ({ commit }, compressFileList) {
      commit('addCompressFileList', compressFileList)
    }
  },
  getters: {
    // 在这里定义计算属性
  }
})
