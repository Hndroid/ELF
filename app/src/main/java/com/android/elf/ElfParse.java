package com.android.elf;

import android.util.Log;

public class ElfParse {

    private static Elf32 mElf32 = new Elf32();

    public static void parseElfHeader(byte[] header, int offset) {
        if (header == null) {
            return;
        }

        /**
         * public byte[] e_ident = new byte[16];
         * public byte[] e_type = new byte[2];
         * public byte[] e_machine = new byte[2];
         * public byte[] e_version = new byte[4];
         * public byte[] e_entry = new byte[4];
         * public byte[] e_phoff = new byte[4];
         * public byte[] e_shoff = new byte[4];
         * public byte[] e_flags = new byte[4];
         * public byte[] e_ehsize = new byte[2];
         * public byte[] e_phentsize = new byte[2];
         * public byte[] e_phnum = new byte[2];
         * public byte[] e_shentsize = new byte[2];
         * public byte[] e_shnum = new byte[2];
         * public byte[] e_shstrndx = new byte[2];
         */
        mElf32.mEhdr.e_ident = ElfUtils.copyBytes(header, 0, 16);
        mElf32.mEhdr.e_type = ElfUtils.copyBytes(header, 16, 2);
        mElf32.mEhdr.e_machine = ElfUtils.copyBytes(header, 18, 2);
        mElf32.mEhdr.e_version = ElfUtils.copyBytes(header, 20, 4);
        mElf32.mEhdr.e_entry = ElfUtils.copyBytes(header, 24, 4);
        mElf32.mEhdr.e_phoff = ElfUtils.copyBytes(header, 28, 4);
        mElf32.mEhdr.e_shoff = ElfUtils.copyBytes(header, 32, 4);
        mElf32.mEhdr.e_flags = ElfUtils.copyBytes(header, 36, 4);
        mElf32.mEhdr.e_ehsize = ElfUtils.copyBytes(header, 40, 2);
        mElf32.mEhdr.e_phentsize = ElfUtils.copyBytes(header, 42, 2);
        mElf32.mEhdr.e_phnum = ElfUtils.copyBytes(header, 44, 2);
        mElf32.mEhdr.e_shentsize = ElfUtils.copyBytes(header, 46, 2);
        mElf32.mEhdr.e_shnum = ElfUtils.copyBytes(header, 48, 2);
        mElf32.mEhdr.e_shstrndx = ElfUtils.copyBytes(header, 50, 2);
        Log.d("Elf32", mElf32.mEhdr.toString());
    }

    public static void parseElfProgramHeader(byte[] header) {
        int phoff = ElfUtils.byteToInt(mElf32.mEhdr.e_phoff);
        int phentsize = ElfUtils.byteToInt(mElf32.mEhdr.e_phentsize);
        int phnum = ElfUtils.byteToInt(mElf32.mEhdr.e_phnum);

        for (int i = 0; i < phnum; ++i) {
            byte[] phItem = ElfUtils.copyBytes(header, i * phentsize + phoff, phentsize);
            mElf32.mPhdrArrayList.add(genProgramHeaderItem(phItem));
        }
    }

    /*
        public byte[] p_type = new byte[4];
        public byte[] p_offset = new byte[4];
        public byte[] p_vaddr = new byte[4];
        public byte[] p_paddr = new byte[4];
        public byte[] p_filesz = new byte[4];
        public byte[] p_memsz = new byte[4];
        public byte[] p_flags = new byte[4];
        public byte[] p_align = new byte[4];
    */
    private static Elf32.Elf32_Phdr genProgramHeaderItem(byte[] item) {
        Elf32.Elf32_Phdr elf32_phdr = new Elf32.Elf32_Phdr();
        elf32_phdr.p_type = ElfUtils.copyBytes(item, 0, 4);
        elf32_phdr.p_offset = ElfUtils.copyBytes(item, 4, 4);
        elf32_phdr.p_vaddr = ElfUtils.copyBytes(item, 8, 4);
        elf32_phdr.p_paddr = ElfUtils.copyBytes(item, 12, 4);
        elf32_phdr.p_filesz = ElfUtils.copyBytes(item, 16, 4);
        elf32_phdr.p_memsz = ElfUtils.copyBytes(item, 20, 4);
        elf32_phdr.p_flags = ElfUtils.copyBytes(item, 24, 4);
        elf32_phdr.p_align = ElfUtils.copyBytes(item, 28, 4);
        Log.d("Elf32", elf32_phdr.toString());
        return elf32_phdr;
    }

    public static void parseElfSectionHeader(byte[] header) {
        int shoff = ElfUtils.byteToInt(mElf32.mEhdr.e_shoff);
        int shentsize = ElfUtils.byteToInt(mElf32.mEhdr.e_shentsize);
        int shnum = ElfUtils.byteToInt(mElf32.mEhdr.e_shnum);

        Log.d("Elf32", "section info: shoff " + shoff);
        for (int i = 0; i < shnum; i++) {
            byte[] shItem = ElfUtils.copyBytes(header, i * shentsize + shoff, shentsize);
            mElf32.mShdrArrayList.add(genSectionHeaderItem(shItem));
        }
    }

    /*
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
     */
    private static Elf32.Elf32_Shdr genSectionHeaderItem(byte[] item) {
        Elf32.Elf32_Shdr elf32Shdr = new Elf32.Elf32_Shdr();
        elf32Shdr.sh_name = ElfUtils.copyBytes(item, 0, 4);
        elf32Shdr.sh_type = ElfUtils.copyBytes(item, 4, 4);
        elf32Shdr.sh_flags = ElfUtils.copyBytes(item, 8, 4);
        elf32Shdr.sh_addr = ElfUtils.copyBytes(item, 12, 4);
        elf32Shdr.sh_offset = ElfUtils.copyBytes(item, 16, 4);
        elf32Shdr.sh_size = ElfUtils.copyBytes(item, 20, 4);
        elf32Shdr.sh_link = ElfUtils.copyBytes(item, 24, 4);
        elf32Shdr.sh_info = ElfUtils.copyBytes(item, 28, 4);
        elf32Shdr.sh_addralign = ElfUtils.copyBytes(item, 32, 4);
        elf32Shdr.sh_entsize = ElfUtils.copyBytes(item, 36, 4);

        Log.d("Elf32", elf32Shdr.toString());
        return elf32Shdr;
    }


}
