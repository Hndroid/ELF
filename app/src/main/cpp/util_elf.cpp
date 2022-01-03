//
// Created by iroot on 2021/11/6.
//

#include <fcntl.h>
#include <unistd.h>
#include <link.h>
#include <malloc.h>
#include <string>

#include "util_elf.h"
#include "util_log.h"


//typedef struct elf32_hdr {
//  unsigned char e_ident[EI_NIDENT];
//  Elf32_Half e_type;
//  Elf32_Half e_machine;
//  Elf32_Word e_version;
//  Elf32_Addr e_entry;
//  Elf32_Off e_phoff;
//  Elf32_Off e_shoff;
//  Elf32_Word e_flags;
//  Elf32_Half e_ehsize;
//  Elf32_Half e_phentsize;
//  Elf32_Half e_phnum;
//  Elf32_Half e_shentsize;
//  Elf32_Half e_shnum;
//  Elf32_Half e_shstrndx;
//} Elf32_Ehdr;
bool ElfUtil::parsingElfHeader(int fd) {
    if (fd < 0) {
        LOGI("ELF: so'fd find exception.");
        return false;
    }

    ElfW(Ehdr)* elf_header = (ElfW(Ehdr)*)malloc(sizeof(ElfW(Ehdr)));
    if (NULL == elf_header) return false;
    ssize_t elf_header_size = read(fd, elf_header, sizeof(ElfW(Ehdr)));
    if (elf_header_size < 0) return false;

    LOGW("ELF: e_ident[EI_MAG0] ~ e_ident[EI_MAG3] 的内容被称为“魔数”，用于标识这是一个 ELF 文件：%d, %d, %d, %d",
         elf_header->e_ident[0], elf_header->e_ident[1], elf_header->e_ident[2], elf_header->e_ident[3]);
    LOGW("ELF: e_ident[EI_CLASS] 指明文件的类型: %d", elf_header->e_ident[4]);
    LOGW("ELF: e_ident[EI_DATA] 指明了目标文件中的数据编码格式: %d", elf_header->e_ident[5]);
    LOGW("ELF: e_ident[EI_VERSION] 指明 ELF 文件头的版本: %d", elf_header->e_ident[6]);
    LOGW("ELF: e_ident[EI_PAD] 到 e_ident[EI_NIDENT-1] 之间的 9 个字节目前暂时不使用，留作以后扩展，在实际的文件中应被填 0 补充:"
         " %d, %d, %d, %d, %d, %d, %d, %d, %d", elf_header->e_ident[7], elf_header->e_ident[8], elf_header->e_ident[9], elf_header->e_ident[10],
         elf_header->e_ident[11], elf_header->e_ident[12], elf_header->e_ident[13], elf_header->e_ident[14],elf_header->e_ident[15]);
    LOGW("ELF: 此字段表明本目标文件属于哪种类型: %d", elf_header->e_type);
    LOGW("ELF: 此字段用于指定该文件适用的处理器体系结构: %d", elf_header->e_machine);
    LOGW("ELF: 此字段指明目标文件的版本: %d", elf_header->e_version);
    LOGW("ELF: 此字段指明程序入口的虚拟地址: %d", elf_header->e_entry);
    LOGW("ELF: 此字段指明程序头表(program header table)开始处在文件中的偏移量: %d", elf_header->e_phoff);
    LOGW("ELF: 此字段指明节头表(section header table)开始处在文件中的偏移量: %d", elf_header->e_shoff);
    LOGW("ELF: 此字段指明节头表(section header table)开始处在文件中的偏移量: %d", elf_header->e_flags);
    LOGW("ELF: 此字段表明 ELF 文件头的大小，以字节为单位: %d", elf_header->e_ehsize);
    LOGW("ELF: 此字段表明在程序头表中每一个表项的大小，以字节为单位: %d", elf_header->e_phentsize);
    LOGW("ELF: 此字段表明程序头表中总共有多少个表项: %d", elf_header->e_phnum);
    LOGW("ELF: 此字段表明在节头表中每一个表项的大小，以字节为单位: %d", elf_header->e_shentsize);
    LOGW("ELF: 此字段表明节头表中总共有多少个表项: %d", elf_header->e_shnum);
    LOGW("ELF: 节头表中与节名字表相对应的表项的索引（节头字符串表索引）: %d", elf_header->e_shstrndx);

    parsingElfDynstr(fd, elf_header);
    parsingElfPHeaderTable(fd, elf_header);
    parsingElfSHeaderTable(fd, elf_header);

    free(elf_header);
    elf_header = NULL;
    return true;
}

bool ElfUtil::parsingElfDynstr(int fd, const ElfW(Ehdr)* elf_header) {
    if (elf_header == NULL || fd < 0) return false;

    // 偏移到 section_header_table 的位置
    off_t elf_section_header_offset = lseek(fd, elf_header->e_shoff*(sizeof(char)), SEEK_SET);
    if (elf_section_header_offset < 0) return false;


    for (int i = 0; i < elf_header->e_shnum; i++) {
        ElfW(Shdr) elf_section_header;
        ssize_t elf_section_header_size = read(fd, &elf_section_header, sizeof(ElfW(Shdr)));
        if (elf_section_header_size < 0) return false;

        // 动态连接符号表
        if (SHT_DYNSYM == elf_section_header.sh_type) {
            dynsym_offset = elf_section_header.sh_offset;
            dynsym_size = elf_section_header.sh_size;
        }

        if (SHT_STRTAB == elf_section_header.sh_type) {
            // 通过 e_shstrndx 找到该 ELF 文件的节区的名字符号表的相关信息。
            // 在下面解析 sectionHeader 获取对应 section 的名字会用到。
            if (i == elf_header->e_shstrndx) {
                strtab_offset = elf_section_header.sh_offset;
                strtab_size = elf_section_header.sh_size;
                continue;
            }
            // 获取dynamic的节名称的字符串表
            dynsym_str_offset = elf_section_header.sh_offset;
            dynsym_str_size = elf_section_header.sh_size;
        }
    }

    LOGE("dynsym 表的开始位置 %d", dynsym_offset);
    LOGE("dynsym 表的整体大小 %d", dynsym_size);
    LOGE("strtab 表的开始位置 %d", dynsym_str_offset);
    LOGE("strtab 表的整体大小 %d", dynsym_str_size);

    off_t offset = lseek(fd, dynsym_str_offset*(sizeof(char)), SEEK_SET);
    if (offset == -1) return false;
    symstr = static_cast<char *>(malloc(dynsym_str_size * (sizeof(char))));

    if (symstr == NULL) return false;

    ssize_t size = read(fd, symstr, strtab_size * (sizeof(char)));

    if (size < 0) return false;

    return true;
}

bool ElfUtil::parsingElfPHeaderTable(int fd, const ElfW(Ehdr)* elf_header) {
    if (elf_header == NULL || fd < 0) return false;

    // 偏移到程序头表处
    off_t elf_pheader_offset = lseek(fd, elf_header->e_phoff*(sizeof(char)), SEEK_SET);
    if (elf_pheader_offset == -1) return false;

    // 解析每一个程序头
    for (int i = 0; i < elf_header->e_phnum; i++) {
        ElfW(Phdr) elf_program_header;
        ssize_t elf_phdr_size = read(fd, &elf_program_header, sizeof(ElfW(Phdr)));
        if (elf_phdr_size < 0) continue;

        LOGE("===========================================================================");
        LOGE("== Program Header  描述段的类型 p_type %x", elf_program_header.p_type);
        LOGE("== Program Header  段偏移 当前所在程序段（Segment）开始地址 p_offset %d", elf_program_header.p_offset);
        LOGE("== Program Header  虚拟地址 段在（内存）中的虚拟地址  p_vaddr %#x", elf_program_header.p_vaddr);
        LOGE("== Program Header  段物理地址 绝对地址  p_paddr %#x", elf_program_header.p_paddr);

        LOGE("== Program Header  在文件中的大小  p_filesz %d", elf_program_header.p_filesz);
        LOGE("== Program Header  在内存中的大小  p_memsz %d", elf_program_header.p_memsz);
        LOGE("== Program Header  段相关标识(read、write、exec)  p_flags %d", elf_program_header.p_flags);
        LOGE("== Program Header  对齐取值 p_align %d", elf_program_header.p_align);
    }

    return true;
}

bool ElfUtil::parsingElfSHeaderTable(int fd, const ElfW(Ehdr)* elf_header) {
    if (elf_header == NULL || fd < 0) return false;


    char *sectionNameTab = static_cast<char *>(malloc(strtab_size * sizeof(char) + 1));
    if (sectionNameTab == NULL) return false;
    memset(sectionNameTab, 0, strtab_size * sizeof(char) + 1);

    // 把 strtab 的偏移到 section 名字符号处;
    off_t _strtab_offset = lseek(fd, strtab_offset * sizeof(char) + 1, SEEK_SET);
    if (_strtab_offset < 0) return false;

    ssize_t strtab_size = read(fd, sectionNameTab, strtab_offset * sizeof(char) + 1);
    if (strtab_size < 0) return false;

    off_t elf_shdr_offset = lseek(fd, elf_header->e_shoff*sizeof(char), SEEK_SET);
    if (elf_shdr_offset == -1) return false;

    for (int i = 0; i < elf_header->e_shnum; i++) {
        ElfW(Shdr) elf_section_header;
        ssize_t elf_shdr_size = read(fd, &elf_section_header, sizeof(ElfW(Shdr)));
        if (elf_shdr_size < 0) return false;

        LOGE(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if (elf_section_header.sh_name == NULL) {
            LOGE(">> section Header's name: SHT_UNDEF");
        } else {
            LOGE(">> section Header's name: %s", elf_section_header.sh_name + sectionNameTab);
        }
        LOGE(">> section Header's type: %d", elf_section_header.sh_type);
        LOGE(">> section Header's addr (如果本节的内容需要映射到进程空间中去，"
             "此成员指定映射的起始地址；如果不需要映射，此值为 0): %d", elf_section_header.sh_type);
        LOGE(">> section Header's addr (指明节的大小，单位是字节。如果该节的类型为 SHT_NOBITS，"
             "此值仍然可能为非零，但没有实际的意义): %d", elf_section_header.sh_addr);
        LOGE(">> section Header's link (此成员是一个索引值，指向节头表中本节所对应的位置): %d", elf_section_header.sh_link);
        LOGE(">> section Header's info (此成员含有此节的附加信息，根据节的类型不同，本成员的意义也有所不同): %d", elf_section_header.sh_info);
        LOGE(">> section Header's addralign (此成员指明本节内容如何对齐字节，即该节的地址应该向多少个字节对齐): %d", elf_section_header.sh_addralign);
        LOGE(">> section Header's entsize (此成员指明本节内容如何对齐字节，即该节的地址应该向多少个字节对齐): %d", elf_section_header.sh_entsize);
    }

    return true;
}

ElfUtil::ElfUtil() {
    dynsym_offset = 0;
    dynsym_size = 0;
    strtab_offset = 0;
    strtab_size = 0;
    dynsym_str_offset = 0;
    dynsym_str_size = 0;
    symstr = NULL;
}

ElfUtil::~ElfUtil() {
    if (symstr != NULL) {
        free(symstr);
        symstr = NULL;
    }
}







