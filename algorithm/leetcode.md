#### 2023/3

##### 1.两数之和(easy)(13)

description：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那 两个 整数，并返回它们的数组下标。你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。你可以按任意顺序返回答案。

example：

输入：nums = [2,7,11,15], target = 9                                                                                                                           输出：[0,1]                                                                                                                                                                         解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 

输入：nums = [3,3], target = 6
输出：[0,1]

提示：

- `2 <= nums.length <= 104`
- `-109 <= nums[i] <= 109`
- `-109 <= target <= 109`
- **只会存在一个有效答案**

法一：暴力枚举(略)                                                                                                                                                                 法二：用HashMap空间换时间

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        int length = nums.length;
        HashMap<Integer,Integer> hs = new HashMap<>();
        for(int i = 0 ; i < length; i++){
            if(hs.containsKey(target - nums[i])){
                return new int []{hs.get(target-nums[i]),i};
            }
              hs.put(nums[i],i);
        }
        return new int [0]；
    }
}
```



##### 1605.给定行和列的和求可行矩阵

给你两个非负整数数组 rowSum 和 colSum ，其中 rowSum[i] 是二维矩阵中第 i 行元素的和， colSum[j] 是第 j 列元素的和。换言之你不知道矩阵里的每个元素，但是你知道每一行和每一列的和。							请找到大小为 rowSum.length x colSum.length 的任意 非负整数 矩阵，且该矩阵满足 rowSum 和 colSum 的要求。																																								请你返回任意一个满足题目要求的二维矩阵，题目保证存在 至少一个 可行矩阵。

设生成的矩阵为 mat。对于只有 1 行的情况，构造 mat\[0][j] = colSum[j]，由于题目保证 sum(rowSum) == sum(colSum)，所以只有 1 行的 mat 是满足题目要求的。假设 m-1 行的 mat 是满足题目要求的，上述构造方案可以满足 mat第一行的 rowSum，且构造的数字不超过相应的 colSum，从而转换成一个 m-1行的子问题。只要 m-1行的 mat 是满足题目要求的，那么 m 行的mat 也是满足题目要求的。
根据数学归纳法，m 行的mat 是满足题目要求的。

```
class Solution {
    public int[][] restoreMatrix(int[] rowSum, int[] colSum) {
        int row = rowSum.length,column = colSum.length;
        int [][]arr = new int [row][column];
        int temp;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                temp = Math.min(rowSum[i],colSum[j]);
                if(rowSum[i] - temp >= 0){
                    rowSum[i] -= temp;
                    colSum[j] -= temp;
                    arr[i][j] = temp;
                }else{
                    break;
                }
            }
        }
        return arr;
    }
}
```