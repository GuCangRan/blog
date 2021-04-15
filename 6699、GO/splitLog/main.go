package main

import (
	"archive/zip"
	"bufio"
	"fmt"
	"io"
	"math"
	"os"
	"path/filepath"
	"strconv"
	"strings"
)

//修改文件大小
var chunkSize int64 = 100 << 20
var filePath string = ""
var fileExtion string = ""

func main() {
	defer stopCmd()

	config := InitConfig("./logconf.conf")
	mySize := config["size"]
	fileExtion = config["sfile_ext"]
	filePath = config["file_path"]

	cSize, err := strconv.Atoi(mySize)
	if err != nil {
		cSize = 10
	}
	chunkSize = int64(cSize << 20)
	fmt.Println(string(mySize), fileExtion, filePath)

	if len(fileExtion) == 0 || len(filePath) == 0 {
		fmt.Println("参数不合法,不做任何处理")
		return
	}

	_, err1 := os.Stat(filePath)
	if err1 != nil {
		fmt.Println("1路径找不到文件,不做任何处理,路径:" + filePath)
		return
	}

	splitLog()
	Zip("./logsplit", "logsplit.zip")
}

/**
开始分割日志
*/
func splitLog() {
	//filePath := "C:\\Users\\绝版小书包\\Desktop\\交接数据库\\1.txt"
	//fileExtion := ".txt"
	fileLoaclPath := "logsplit/"
	//chunkSize = 100 << 20

	os.RemoveAll(fileLoaclPath)
	os.Mkdir(fileLoaclPath, os.ModePerm)
	fileInfo, err := os.Stat(filePath)
	if err != nil {
		fmt.Println(err)
	}

	num := int(math.Ceil(float64(fileInfo.Size()) / float64(chunkSize)))

	fi, err := os.OpenFile(filePath, os.O_RDONLY, os.ModePerm)
	if err != nil {
		fmt.Println(err)
		return
	}
	b := make([]byte, chunkSize)
	var i int64 = 1
	for ; i <= int64(num); i++ {

		fi.Seek((i-1)*(chunkSize), 0)

		if len(b) > int((fileInfo.Size() - (i-1)*chunkSize)) {
			b = make([]byte, fileInfo.Size()-(i-1)*chunkSize)
		}

		fi.Read(b)

		f, err := os.OpenFile("./"+fileLoaclPath+strconv.Itoa(int(i))+fileExtion, os.O_CREATE|os.O_WRONLY, os.ModePerm)
		if err != nil {
			fmt.Println(err)
			return
		}
		f.Write(b)
		f.Close()
	}
	fi.Close()

	fmt.Println("日志执行结束")

	//写入日志
	//fii, err := os.OpenFile(filePath, os.O_CREATE|os.O_WRONLY|os.O_APPEND, os.ModePerm)
	//if err != nil {
	//	fmt.Println(err)
	//	return
	//}
	//for i := 1; i <= num; i++ {
	//	f, err := os.OpenFile("./"+fileLoaclPath+strconv.Itoa(int(i))+fileExtion, os.O_RDONLY, os.ModePerm)
	//	if err != nil {
	//		fmt.Println(err)
	//		return
	//	}
	//	b, err := ioutil.ReadAll(f)
	//	if err != nil {
	//		fmt.Println(err)
	//		return
	//	}
	//	fii.Write(b)
	//	f.Close()
	//}
}

//读取key=value类型的配置文件
func InitConfig(path string) map[string]string {
	config := make(map[string]string)

	f, err := os.Open(path)
	defer f.Close()
	if err != nil {
		panic(err)
	}

	r := bufio.NewReader(f)
	for {
		b, _, err := r.ReadLine()
		if err != nil {
			if err == io.EOF {
				break
			}
			panic(err)
		}
		s := strings.TrimSpace(string(b))
		index := strings.Index(s, "=")
		if index < 0 {
			continue
		}
		key := strings.TrimSpace(s[:index])
		if len(key) == 0 {
			continue
		}
		value := strings.TrimSpace(s[index+1:])
		if len(value) == 0 {
			continue
		}
		config[key] = value
	}
	return config
}

func stopCmd() {
	var str string
	fmt.Printf("请输入任意内容退出窗口")
	fmt.Scan(&str)
	fmt.Printf("str: %s", str)
}

// 打包成zip文件
func Zip(src_dir string, zip_file_name string) {

	// 预防：旧文件无法覆盖
	os.RemoveAll(zip_file_name)

	// 创建：zip文件
	zipfile, _ := os.Create(zip_file_name)
	defer zipfile.Close()

	// 打开：zip文件
	archive := zip.NewWriter(zipfile)
	defer archive.Close()

	// 遍历路径信息
	filepath.Walk(src_dir, func(path string, info os.FileInfo, _ error) error {

		// 如果是源路径，提前进行下一个遍历
		if path == src_dir {
			return nil
		}

		// 获取：文件头信息
		header, _ := zip.FileInfoHeader(info)
		header.Name = strings.TrimPrefix(path, src_dir+`\`)

		// 判断：文件是不是文件夹
		if info.IsDir() {
			header.Name += `/`
		} else {
			// 设置：zip的文件压缩算法
			header.Method = zip.Deflate
		}

		// 创建：压缩包头部信息
		writer, _ := archive.CreateHeader(header)
		if !info.IsDir() {
			file, _ := os.Open(path)
			defer file.Close()
			io.Copy(writer, file)
		}
		return nil
	})
}
