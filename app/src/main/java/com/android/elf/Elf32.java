package com.android.elf;

import java.util.ArrayList;

public class Elf32 {

    public Elf32_Ehdr mEhdr;
    public ArrayList<Elf32_Phdr> mPhdrArrayList;
    public ArrayList<Elf32_Shdr> mShdrArrayList;

    public Elf32() {
        mEhdr = new Elf32_Ehdr();
        mPhdrArrayList = new ArrayList<>();
        mShdrArrayList = new ArrayList<>();
    }

    public class Elf32_Ehdr {
        public byte[] e_ident = new byte[16];
        public byte[] e_type = new byte[2];
        public byte[] e_machine = new byte[2];
        public byte[] e_version = new byte[4];
        public byte[] e_entry = new byte[4];
        public byte[] e_phoff = new byte[4];
        public byte[] e_shoff = new byte[4];
        public byte[] e_flags = new byte[4];
        public byte[] e_ehsize = new byte[2];
        public byte[] e_phentsize = new byte[2];
        public byte[] e_phnum = new byte[2];
        public byte[] e_shentsize = new byte[2];
        public byte[] e_shnum = new byte[2];
        public byte[] e_shstrndx = new byte[2];

        @Override
        public String toString() {
            return "Elf32_Ehdr{\n" +
                    "e_ident= " + ElfUtils.bytesToHexString(e_ident) +
                    ",\ne_type= " + ElfUtils.bytesToHexString(e_type) +
                    ",\ne_machine= " + ElfUtils.bytesToHexString(e_machine) +
                    ",\ne_version= " + ElfUtils.bytesToHexString(e_version) +
                    ",\ne_entry= " + ElfUtils.bytesToHexString(e_entry) +
                    ",\ne_phoff= " + ElfUtils.bytesToHexString(e_phoff) +
                    ",\ne_shoff= " + ElfUtils.bytesToHexString(e_shoff) +
                    ",\ne_flags= " + ElfUtils.bytesToHexString(e_flags) +
                    ",\ne_ehsize= " + ElfUtils.bytesToHexString(e_ehsize) +
                    ",\ne_phentsize= " + ElfUtils.bytesToHexString(e_phentsize) +
                    ",\ne_phnum= " + ElfUtils.bytesToHexString(e_phnum) +
                    ",\ne_shentsize= " + ElfUtils.bytesToHexString(e_shentsize) +
                    ",\ne_shnum= " + ElfUtils.bytesToHexString(e_shnum) +
                    ",\ne_shstrndx= " + ElfUtils.bytesToHexString(e_shstrndx) +
                    '}';
        }
    }

    public static class Elf32_Phdr {
        public byte[] p_type = new byte[4];
        public byte[] p_offset = new byte[4];
        public byte[] p_vaddr = new byte[4];
        public byte[] p_paddr = new byte[4];
        public byte[] p_filesz = new byte[4];
        public byte[] p_memsz = new byte[4];
        public byte[] p_flags = new byte[4];
        public byte[] p_align = new byte[4];

        @Override
        public String toString() {
            return "\n------------------------------\nElf32_Phdr{\n" +
                    "p_type= " + ElfUtils.bytesToHexString(p_type) +
                    ",\np_offset= " + ElfUtils.bytesToHexString(p_offset) +
                    ",\np_vaddr= " + ElfUtils.bytesToHexString(p_vaddr) +
                    ",\np_paddr= " + ElfUtils.bytesToHexString(p_paddr) +
                    ",\np_filesz= " + ElfUtils.bytesToHexString(p_filesz) +
                    ",\np_memsz= " + ElfUtils.bytesToHexString(p_memsz) +
                    ",\np_flags= " + ElfUtils.bytesToHexString(p_flags) +
                    ",\np_align= " + ElfUtils.bytesToHexString(p_align) +
                    '}';
        }
    }

    /*
        Elf32_Word    sh_name;
        Elf32_Word    sh_type;
        Elf32_Word    sh_flags;
        Elf32_Addr    sh_addr;
        Elf32_Off     sh_offset;
        Elf32_Word    sh_size;
        Elf32_Word    sh_link;
        Elf32_Word    sh_info;
        Elf32_Word    sh_addralign;
        Elf32_Word    sh_entsize;
    */
    public static class Elf32_Shdr {
        public byte[] sh_name = new byte[4];
        public byte[] sh_type = new byte[4];
        public byte[] sh_flags = new byte[4];
        public byte[] sh_addr = new byte[4];
        public byte[] sh_offset = new byte[4];
        public byte[] sh_size = new byte[4];
        public byte[] sh_link = new byte[4];
        public byte[] sh_info = new byte[4];
        public byte[] sh_addralign = new byte[4];
        public byte[] sh_entsize = new byte[4];

        @Override
        public String toString() {
            return "\n******************************\nElf32_Shdr{" +
                    "\nsh_name=" + ElfUtils.bytesToHexString(sh_name) +
                    ",\nsh_type=" + ElfUtils.bytesToHexString(sh_type) +
                    ",\nsh_flags=" + ElfUtils.bytesToHexString(sh_flags) +
                    ",\nsh_addr=" + ElfUtils.bytesToHexString(sh_addr) +
                    ",\nsh_offset=" + ElfUtils.bytesToHexString(sh_offset) +
                    ",\nsh_size=" + ElfUtils.bytesToHexString(sh_size) +
                    ",\nsh_link=" + ElfUtils.bytesToHexString(sh_link) +
                    ",\nsh_info=" + ElfUtils.bytesToHexString(sh_info) +
                    ",\nsh_addralign=" + ElfUtils.bytesToHexString(sh_addralign) +
                    ",\nsh_entsize=" + ElfUtils.bytesToHexString(sh_entsize) +
                    '}';
        }
    }





}

