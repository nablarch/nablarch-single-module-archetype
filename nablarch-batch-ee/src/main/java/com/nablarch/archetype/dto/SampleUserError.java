package com.nablarch.archetype.dto;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 精査エラーが発生したレコードをエラー用のテーブルに
 * 投入する際に使用するDtoクラス。
 * ETL機能で使用する。
 *
 * @deprecated TODO 疎通確認が終わったら削除すること。
 */
@Entity
@Table(name = "SAMPLE_USER_ERROR")
public class SampleUserError extends SampleUserDto {

}
