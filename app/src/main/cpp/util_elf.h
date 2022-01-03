//
// Created by iroot on 2021/11/6.
//

#ifndef ELF_UTIL_ELF_H
#define ELF_UTIL_ELF_H

#include "util_elf.h"
#include <link.h>

class ElfUtil {


public:
    ElfUtil();
    ~ElfUtil();

    /**
     * 解析 ELF 文件头
     *
     * @param fd                （文件描述符）文件句柄
     * @return                  解析 ELF 文件头成功返回 true
     */
    bool parsingElfHeader(int fd);

    /**
     * 解析 ELF 符号表
     *
     * @param fd                文件句柄
     * @param elf_header        ELF 文件头指针
     * @return                  解析 ELF 符号表成功返回 true
     */
    bool parsingElfDynstr(int fd, const ElfW(Ehdr)* elf_header);

    /**
     * 解析 ELF 的节头表
     *
     * @param fd                 文件句柄
     * @param elf_header         ELF 文件头指针
     * @return                   解析 ELF 节头成功返回 true
     */
    bool parsingElfSHeaderTable(int fd, const ElfW(Ehdr)* elf_header);

    /**
     * 解析 ELF 的程序头表
     *
     * @param fd                文件句柄
     * @param elf_header        ELF 文件头指针
     * @return                  解析 ELF 程序头成功返回 true
     */
    bool parsingElfPHeaderTable(int fd, const ElfW(Ehdr)* elf_header);

private:
    // 动态连接的符号表的偏移
    uintptr_t dynsym_offset;
    // 动态连接的符号表的大小
    uint32_t dynsym_size;

    // 字符串表的偏移
    uintptr_t strtab_offset;
    // 字符串表的大小
    uint32_t strtab_size;

    //
    uintptr_t dynsym_str_offset;
    uint32_t dynsym_str_size;

    char* symstr;
};


#endif //ELF_UTIL_ELF_H
