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



