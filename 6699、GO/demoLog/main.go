package main

import (
	"fmt"
	"io/ioutil"
	"os"
)

func main() {
	dir, _ := os.Getwd()
	fmt.Println("当前路径：", dir)
	path := getConfPath()
	_, err2 := os.Stat(path)
	fmt.Println(err2)
	if err2 != nil {
		fmt.Println("路径未能检测到:" + path)
		stopCmd()
		return
	}
	delDir(path, false)
	stopCmd()
}

func delDir(path string, isDel bool) {
	files, err := ioutil.ReadDir(path)
	if err != nil {
		panic(err)
		return
	}
	// 获取文件，并输出它们的名字
	for _, file := range files {
		myFileName := file.Name()
		myLen := len(myFileName)
		filePath := path + myFileName
		if !IsDir(filePath) {
			continue
		}
		//fmt.Println("扫描文件" + myFileName + ";长度=" + myLen)
		fmt.Printf("扫描文件夹:%s  文件长度: %d \r\n", myFileName, myLen)

		if myLen > 6 && isDel {
			fmt.Println("执行删除:" + filePath)
			os.RemoveAll(filePath)
		} else {
			delDir(filePath+`\`, true)
		}

	}
}

func stopCmd() {
	var str string
	fmt.Printf("请输入任意内容退出窗口")
	fmt.Scan(&str)
	fmt.Printf("str: %s", str)
}

func getConfPath() string {
	data, err := ioutil.ReadFile("./conf.txt")
	if err != nil {
		fmt.Println("File reading error", err)
		return ""
	}
	fmt.Println("要检测删除的路径:", string(data))
	return string(data)
}

// 判断所给路径是否为文件夹
func IsDir(path string) bool {
	s, err := os.Stat(path)
	if err != nil {
		return false
	}
	return s.IsDir()
}

// 判断所给路径是否为文件
func IsFile(path string) bool {
	return !IsDir(path)
}
