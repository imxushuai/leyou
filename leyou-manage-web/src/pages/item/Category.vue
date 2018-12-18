<template>
  <v-card>
      <v-flex xs12 sm10>
        <v-tree url="/item/category/list"
                :isEdit="isEdit"
                @handleAdd="handleAdd"
                @handleEdit="handleEdit"
                @handleDelete="handleDelete"
                @handleClick="handleClick"
        />
      </v-flex>
  </v-card>
</template>

<script>
  export default {
    name: "category",
    data() {
      return {
        isEdit:true
      }
    },
    methods: {
      handleAdd(node) {
        // console.log("add .... ");
        console.log(node);
        this.$http.post('/item/category', {
          "name": node.name,
          "sort": node.sort,
          "parentId": node.parentId,
          "isParent": node.isParent
        }).then(response => {
          if (response.status === 201) {
            this.$message.info('添加成功');
          } else {
            this.$message.error(response['message']);
          }
        }).catch(() => {
          this.$message.error('添加失败');
        })
      },
      handleEdit(id, name) {
        console.log("edit... id: " + id + ", name: " + name)
        // 这个树菜单的编辑，我也没玩明白。23333333

      },
      handleDelete(id) {
        // console.log("delete ... " + id)
        this.$http.delete('/item/category/' + id)
          .then(response => {
            if (response.status === 200) {
              this.$message.info("删除成功")
            } else {
              this.$message.error(response['message'])
            }
          })
          .catch(() => {
            this.$message.error("删除失败")
          })
      },
      handleClick(node) {
      }
    }
  };
</script>

<style scoped>

</style>
