package org.sereinfish.catcat.image.skiko.tools.element.measure

import kotlin.math.abs

/**
 * 元素的大小模式
 *
 */
class ElementSizeMode(
    private var value: Int
) {
    companion object{
        val Auto =          ElementSizeMode(0b001001) // 默认值，元素大小自动计算，超过父布局裁剪显示，没超过根据元素大小显示
        val AutoWidth =     ElementSizeMode(0b000001) // 自动宽度
        val AutoHeight =    ElementSizeMode(0b001000) // 自动高度
        val Value =         ElementSizeMode(0b010010) // 根据手动设置的大小显示
        val ValueWidth =    ElementSizeMode(0b000010)
        val ValueHeight =   ElementSizeMode(0b010000)
        val MaxFill =       ElementSizeMode(0b100100) // 占满父布局
        val MaxWidth =      ElementSizeMode(0b000100) // 最大宽度
        val MaxHeight =     ElementSizeMode(0b100000) // 最大高度
    }

    infix fun and(other: ElementSizeMode): ElementSizeMode {
        return ElementSizeMode(value.and(
            when(other){
                AutoWidth, ValueWidth, MaxWidth -> 0b111.inv()
                AutoHeight, ValueHeight, MaxHeight -> 0b111
                Auto, Value, MaxFill -> Int.MAX_VALUE
                else -> error("未知的 ElementSizeMode")
            }
        ).or(other.value))
    }

    infix fun not(other: ElementSizeMode): ElementSizeMode {
        return ElementSizeMode(value.and(other.value.inv()))
    }

    /**
     * 解构模式
     */
    fun decode(): List<ElementSizeMode> {
        var v = abs(value)
        var step = 0
        return buildList {
            while (v > 0){
                if (v.and(0x1) == 0x1){
                    add(ElementSizeMode(0x1.shl(step)))
                }
                v = v.shr(1)
                step ++
            }
        }
    }

    /**
     * 包含某个模式
     */
    fun contain(mode: ElementSizeMode): Boolean {
        return decode().contains(mode)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ElementSizeMode

        return value == other.value
    }

    override fun toString(): String {
        return "ElementSizeMode(value=${value.toString(2)})"
    }

    override fun hashCode(): Int {
        return value
    }
}