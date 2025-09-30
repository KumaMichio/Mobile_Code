import kotlin.math.abs

class PhanSo(private var tu: Int, private var mau: Int) {

    init {
        if (mau == 0) {
            throw IllegalArgumentException("Mẫu số không thể bằng 0")
        }
    }

    // Nhập phân số từ bàn phím
    fun nhap() {
        while (true) {
            print("Nhập tử số: ")
            tu = readln().toInt()
            print("Nhập mẫu số: ")
            mau = readln().toInt()
            if (mau != 0) break
            println("Mẫu số không thể bằng 0. Vui lòng nhập lại!")
        }
    }

    // In phân số
    fun xuat() {
        println("$tu/$mau")
    }

    // Tối giản phân số
    fun toiGian() {
        val gcd = ucln(abs(tu), abs(mau))
        tu /= gcd
        mau /= gcd
        // chuẩn hóa: nếu mẫu âm => đưa dấu lên tử
        if (mau < 0) {
            tu = -tu
            mau = -mau
        }
    }

    // So sánh với phân số khác
    fun soSanh(ps: PhanSo): Int {
        val left = tu * ps.mau
        val right = ps.tu * mau
        return when {
            left < right -> -1
            left == right -> 0
            else -> 1
        }
    }

    // Tính tổng với một phân số khác
    fun cong(ps: PhanSo): PhanSo {
        val tuMoi = tu * ps.mau + ps.tu * mau
        val mauMoi = mau * ps.mau
        val kq = PhanSo(tuMoi, mauMoi)
        kq.toiGian()
        return kq
    }

    // Trả về giá trị thực của phân số
    fun giaTri(): Double = tu.toDouble() / mau.toDouble()

    companion object {
        // Ước chung lớn nhất
        fun ucln(a: Int, b: Int): Int {
            return if (b == 0) a else ucln(b, a % b)
        }
    }
}

fun main() {
    print("Nhập số lượng phân số: ")
    val n = readln().toInt()
    val arr = Array(n) { PhanSo(1, 1) }

    // Nhập mảng phân số
    for (i in arr.indices) {
        println("Nhập phân số thứ ${i + 1}:")
        val ps = PhanSo(1, 1)
        ps.nhap()
        arr[i] = ps
    }

    println("\nMảng phân số vừa nhập:")
    arr.forEach { it.xuat() }

    println("\nMảng phân số sau khi tối giản:")
    arr.forEach {
        it.toiGian()
        it.xuat()
    }

    // Tính tổng các phân số
    var tong = PhanSo(0, 1)
    arr.forEach { tong = tong.cong(it) }
    print("\nTổng các phân số = ")
    tong.xuat()

    // Tìm phân số lớn nhất
    val maxPs = arr.maxByOrNull { it.giaTri() }
    print("\nPhân số lớn nhất: ")
    maxPs?.xuat()

    // Sắp xếp giảm dần
    val sortedArr = arr.sortedWith { a, b -> b.soSanh(a) }
    println("\nMảng sau khi sắp xếp giảm dần:")
    sortedArr.forEach { it.xuat() }
}
