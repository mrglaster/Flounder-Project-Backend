# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: study_service.proto
# Protobuf Python Version: 5.26.1
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x13study_service.proto\x12\x06module\"\xc3\x01\n\x15ModuleCreationRequest\x12\x11\n\tfile_name\x18\x01 \x01(\t\x12\x0e\n\x06\x61uthor\x18\x02 \x01(\t\x12\r\n\x05title\x18\x03 \x01(\t\x12\x10\n\x08language\x18\x04 \x01(\t\x12\x10\n\x08wordlist\x18\x05 \x03(\t\x12\x14\n\x0ctranslations\x18\x06 \x03(\t\x12\x1d\n\x15translations_language\x18\x07 \x01(\t\x12\x0c\n\x04icon\x18\x08 \x01(\t\x12\x11\n\tmodule_id\x18\t \x01(\x05\"(\n\x16ModuleCreationResponse\x12\x0e\n\x06status\x18\x01 \x01(\t2^\n\rModuleService\x12M\n\x0c\x43reateModule\x12\x1d.module.ModuleCreationRequest\x1a\x1e.module.ModuleCreationResponseb\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'study_service_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  DESCRIPTOR._loaded_options = None
  _globals['_MODULECREATIONREQUEST']._serialized_start=32
  _globals['_MODULECREATIONREQUEST']._serialized_end=227
  _globals['_MODULECREATIONRESPONSE']._serialized_start=229
  _globals['_MODULECREATIONRESPONSE']._serialized_end=269
  _globals['_MODULESERVICE']._serialized_start=271
  _globals['_MODULESERVICE']._serialized_end=365
# @@protoc_insertion_point(module_scope)