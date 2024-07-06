<template>
  <div>
    <div>假设图片的灰度值有：a1,a2,a3,a4,a5,a6,a7,a8</div>
    <div>它们的概率分别为：0.2,0.19,0.18,0.17,0.15,0.1,0.005,0.005</div>
    <button @click="nextStep">Next Step</button>
    <ul class="tree">
      <li>
        <details open>
          <summary>Huffman编码过程</summary>
          <ul class="huffman">
            <li v-for="(item, index) in huffmanList" :key="index" :id="index + 1" v-html="item"></li>
          </ul>
        </details>
      </li>
    </ul>
    <div v-if="showHuffman" id="huffman">
      <ul>
        <li>编码表如下</li>
        <li>a1-01</li>
        <li>a2-00</li>
        <li>a3-111</li>
        <li>a4-110</li>
        <li>a5-101</li>
        <li>a6-1001</li>
        <li>a7-10001</li>
        <li>a8-10000</li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      huffmanList: [
        'a1-0.2',
        'a2-0.19',
        'a3-0.18',
        'a4-0.17',
        'a5-0.15',
        'a6-0.1',
        'a7-0.005',
        'a8-0.005'
      ],
      step: 1,
      showHuffman: false
    }
  },
  methods: {
    nextStep () {
      if (this.step === 1) {
        this.huffmanList[6] = `
          <details open>
            <summary>0.01</summary>
            <ul>
              <li>a7-0.005</li>
              <li>a8-0.005</li>
            </ul>
          </details>
        `
        this.huffmanList.splice(7, 1)
        this.step++
      } else if (this.step === 2) {
        this.huffmanList[5] = `
          <details open>
            <summary>0.11</summary>
            <ul>
              <li>a6-0.1</li>
              <li><details open>
                <summary>0.01</summary>
                <ul>
                  <li>a7-0.005</li>
                  <li>a8-0.005</li>
                </ul>
              </details></li>
            </ul>
          </details>
        `
        this.huffmanList.splice(6, 1)
        this.step++
      } else if (this.step === 3) {
        this.huffmanList.unshift(`
          <details open>
            <summary>0.26</summary>
            <ul>
              <li>a5-0.15</li>
              <li><details open>
                <summary>0.11</summary>
                <ul>
                  <li>a6-0.1</li>
                  <li><details open>
                    <summary>0.01</summary>
                    <ul>
                      <li>a7-0.005</li>
                      <li>a8-0.005</li>
                    </ul>
                  </details></li>
                </ul>
              </details></li>
            </ul>
          </details>
        `)
        this.huffmanList.splice(5, 2)
        this.step++
      } else if (this.step === 4) {
        this.huffmanList.unshift(`
          <details open>
            <summary>0.35</summary>
            <ul>
              <li>a3-0.18</li>
              <li>a4-0.17</li>
            </ul>
          </details>
        `)
        this.huffmanList.splice(4, 2)
        this.step++
      } else if (this.step === 5) {
        this.huffmanList.unshift(`
          <details open>
            <summary>0.39</summary>
            <ul>
              <li>a1-0.2</li>
              <li>a2-0.19</li>
            </ul>
          </details>
        `)
        this.huffmanList.splice(3, 2)
        this.step++
      } else if (this.step === 6) {
        this.huffmanList.unshift(`
          <details open>
            <summary>0.61</summary>
            <ul>
              <li>
                <details open>
                  <summary>0.35</summary>
                  <ul>
                    <li>a3-0.18</li>
                    <li>a4-0.17</li>
                  </ul>
                </details>
              </li>
              <li>
                <details open>
                  <summary>0.26</summary>
                  <ul>
                    <li>a5-0.15</li>
                    <li><details open>
                      <summary>0.11</summary>
                      <ul>
                        <li>a6-0.1</li>
                        <li><details open>
                          <summary>0.01</summary>
                          <ul>
                            <li>a7-0.005</li>
                            <li>a8-0.005</li>
                          </ul>
                        </details></li>
                      </ul>
                    </details></li>
                  </ul>
                </details>
              </li>
            </ul>
          </details>
        `)
        this.huffmanList.splice(2, 2)
        this.step++
      } else if (this.step === 7) {
        this.huffmanList[0] = `
          <details open>
            <summary>1</summary>
            <ul>
              <li>
                <details open>
                  <summary>0.61</summary>
                  <ul>
                    <li>
                      <details open>
                        <summary>0.35</summary>
                        <ul>
                          <li>a3-0.18</li>
                          <li>a4-0.17</li>
                        </ul>
                      </details>
                    </li>
                    <li>
                      <details open>
                        <summary>0.26</summary>
                        <ul>
                          <li>a5-0.15</li>
                          <li><details open>
                            <summary>0.11</summary>
                            <ul>
                              <li>a6-0.1</li>
                              <li><details open>
                                <summary>0.01</summary>
                                <ul>
                                  <li>a7-0.005</li>
                                  <li>a8-0.005</li>
                                </ul>
                              </details></li>
                            </ul>
                          </details></li>
                        </ul>
                      </details>
                    </li>
                  </ul>
                </details>
              </li>
              <li>
                <details open>
                  <summary>0.39</summary>
                  <ul>
                    <li>a1-0.2</li>
                    <li>a2-0.19</li>
                  </ul>
                </details>
              </li>
            </ul>
          </details>
        `
        this.huffmanList.splice(1, 1)
        this.step++
      } else if (this.step === 8) {
        this.huffmanList[0] = `
          <details open>
            <summary>1</summary>
            <ul>
              <li>
                <details open>
                  <summary>(1) 0.61</summary>
                  <ul>
                    <li>
                      <details open>
                        <summary>(1) 0.35</summary>
                        <ul>
                          <li>(1) a3-0.18</li>
                          <li>(0) a4-0.17</li>
                        </ul>
                      </details>
                    </li>
                    <li>
                      <details open>
                        <summary>(0) 0.26</summary>
                        <ul>
                          <li>(1) a5-0.15</li>
                          <li><details open>
                            <summary>(0) 0.11</summary>
                            <ul>
                              <li>(1) a6-0.1</li>
                              <li><details open>
                                <summary>(0) 0.01</summary>
                                <ul>
                                  <li>(1) a7-0.005</li>
                                  <li>(0) a8-0.005</li>
                                </ul>
                              </details></li>
                            </ul>
                          </details></li>
                        </ul>
                      </details>
                    </li>
                  </ul>
                </details>
              </li>
              <li>
                <details open>
                  <summary>(0) 0.39</summary>
                  <ul>
                    <li>(1) a1-0.2</li>
                    <li>(0) a2-0.19</li>
                  </ul>
                </details>
              </li>
            </ul>
          </details>
        `
        this.showHuffman = true
      }
    }
  }
}
</script>

<style >
.tree {
  --spacing: 1.5rem;
  --radius: 10px;
}
.tree li {
  display: block;
  position: relative;
  padding-left: calc(2 * var(--spacing) - var(--radius) - 2px);
}
.tree ul {
  margin-left: calc(var(--radius) - var(--spacing));
  padding-left: 0;
}
.tree ul li {
  border-left: 2px solid #171616;
}
.tree ul li:last-child {
  border-color: transparent;
}
.tree ul li::before {
  content: '';
  display: block;
  position: absolute;
  top: calc(var(--spacing) / -2);
  left: -2px;
  width: calc(var(--spacing) + 2px);
  height: calc(var(--spacing) + 1px);
  border: solid #171616;
  border-width: 0 0 2px 2px;
}
.tree summary {
  display: block;
  pointer-events: none;
}
.tree summary::marker,
.tree summary::-webkit-details-marker {
  display: none;
}
.tree summary:focus {
  outline: none;
}
.tree summary:focus-visible {
  outline: 1px dotted #000;
}

</style>
