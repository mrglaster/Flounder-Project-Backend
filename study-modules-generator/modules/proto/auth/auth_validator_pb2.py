# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: proto/auth_validator.proto
# Protobuf Python Version: 5.26.1
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x1aproto/auth_validator.proto\x12\x15ru.flounder.auth.grpc\"%\n\x14TokenValidateRequest\x12\r\n\x05token\x18\x01 \x01(\t\"&\n\x15TokenValidateResponse\x12\r\n\x05valid\x18\x01 \x01(\x08\x32\x86\x01\n\x16TokenValidationService\x12l\n\rValidateToken\x12+.ru.flounder.auth.grpc.TokenValidateRequest\x1a,.ru.flounder.auth.grpc.TokenValidateResponse\"\x00\x42)\n\x15ru.flounder.auth.grpcB\x0eTokenValidatorP\x01\x62\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'proto.auth_validator_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  _globals['DESCRIPTOR']._loaded_options = None
  _globals['DESCRIPTOR']._serialized_options = b'\n\025ru.flounder.auth.grpcB\016TokenValidatorP\001'
  _globals['_TOKENVALIDATEREQUEST']._serialized_start=53
  _globals['_TOKENVALIDATEREQUEST']._serialized_end=90
  _globals['_TOKENVALIDATERESPONSE']._serialized_start=92
  _globals['_TOKENVALIDATERESPONSE']._serialized_end=130
  _globals['_TOKENVALIDATIONSERVICE']._serialized_start=133
  _globals['_TOKENVALIDATIONSERVICE']._serialized_end=267
# @@protoc_insertion_point(module_scope)